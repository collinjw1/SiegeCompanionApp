package edu.rosehulman.collinjw.siegecompanionapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import edu.rosehulman.collinjw.siegecompanionapp.ui.gallery.GalleryFragment
import edu.rosehulman.collinjw.siegecompanionapp.ui.home.HomeFragment
import edu.rosehulman.collinjw.siegecompanionapp.ui.tools.ToolsFragment
import kotlinx.android.synthetic.main.change_username_dialog.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

private const val REQUEST_TAKE_PHOTO = 2
private const val RC_CHOOSE_PICTURE = 3

class MainActivity : AppCompatActivity(), DirectoryFragment.OnDirectoryListener,
    HomeFragment.OnHomeListener, GalleryFragment.OnSearchListener,
    ToolsFragment.OnToolsListener, SplashFragment.OnLoginButtonPressedListener, SubmitTipPage.SubmitFragmentInteractionListener {


    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private val RC_SIGN_IN = 1
    val auth = FirebaseAuth.getInstance()
    val userDataRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USERDATA_COLLECTION)
    private val storageRef = FirebaseStorage
        .getInstance()
        .reference
        .child("media")
    val postsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.POSTS_COLLECTION)
    var scUserData: UserDataObject? = null
    private lateinit var currentPhotoPath: String
    private lateinit var currentPostObject: PostObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        initializeListeners()






    }

    fun setUserData() {

        userDataRef.document(auth.currentUser!!.uid).get().addOnSuccessListener {
            Log.d("fuck", it.toString())
            if (it["doc"] != null) {
                scUserData = UserDataObject.fromSnapshot(it)
            }
            if (scUserData == null) {
                scUserData = UserDataObject(auth.currentUser!!.uid, "")
                userDataRef.document(auth.currentUser!!.uid).set(UserDataObject(auth.currentUser!!.uid, ""))
            }
            updateNavDrawer()
        }
    }

    fun updateNavDrawer() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        var hv = navView.getHeaderView(0)
        var navUsername = hv.findViewById<TextView>(R.id.navdrawer_account_username)
        navUsername.text = auth.currentUser?.displayName
        var siegeUsername = hv.findViewById<TextView>(R.id.navdrawer_siege_username)
        siegeUsername.text = scUserData?.siegeUsername
    }

    private fun initializeListeners() {
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            if (user != null) {
                setUserData()
                switchToHomeFragment(user.uid)
            } else {
                switchToSplashFragment()
            }
        }

    }

    private fun switchToSplashFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, SplashFragment())
        ft.commit()
    }

    private fun switchToHomeFragment(uid: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, HomeFragment())
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onDirectorySelected(searchTag: String) {
        val postFragment = PostListFragment(searchTag)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, postFragment)
        ft.addToBackStack("detail2")
        ft.commit()
    }

    override fun onButtonSelected(s: String) {
        val switchTo: Fragment
        if (s == Constants.SUBMIT) {
            switchTo = SubmitTipPage()
        } else if (s == Constants.HIGHLIGHT_REEL) {
            switchTo = PostListFragment(Constants.HIGHLIGHT_REEL)
        } else if (s == Constants.STATS){
            switchTo = UserStatPage(scUserData?.siegeUsername)
        } else {
            switchTo = DirectoryFragment(s)
        }
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, switchTo)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onSearch(searchTag: String) {
        val postFragment = PostListFragment(searchTag)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, postFragment)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    override fun onLoginButtonPressed() {
        launchLoginUI()

    }

    override fun onSettingsSelected(s: String) {
        if (s == "Logout") {
            auth.signOut()
        }
    }


    override fun showChangeUsernameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.change_siege_username)
        val view = LayoutInflater.from(this).inflate(R.layout.change_username_dialog, null,false)
        view.siege_username_edit_text.setText(scUserData!!.siegeUsername)
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->

            val newUsername = view.siege_username_edit_text.text.toString()
            scUserData!!.siegeUsername = newUsername
            userDataRef.document(auth.currentUser!!.uid).set(scUserData!!)
            updateNavDrawer()
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    private fun launchLoginUI() {
        // For details, see https://firebase.google.com/docs/auth/android/firebaseui#sign_in
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        startActivityForResult(loginIntent, RC_SIGN_IN)

    }

    override fun onPictureButtonPressed(po: PostObject) {
//        dispatchTakePictureIntent()
        currentPostObject = po
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a photo source")
        builder.setMessage("Would you like to take a new picture?\nOr choose an existing one?")
        builder.setPositiveButton("Take Picture") { _, _ ->
            Log.d(Constants.SUBMIT, "take picture chosen")
            dispatchTakePictureIntent()
        }

        builder.setNegativeButton("Choose Picture") { _, _ ->
            dispatchChoosePictureIntent()
        }
        builder.create().show()
    }

    private fun dispatchTakePictureIntent() {

        Log.d(Constants.SUBMIT, "take picture dispatch")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "edu.rosehulman.collinjw.siegecompanionapp",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    Log.d(Constants.SUBMIT, "pic intent launched")
                }
            }
        }
    }

    private fun dispatchChoosePictureIntent() {
        val choosePictureIntent = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        choosePictureIntent.addCategory(Intent.CATEGORY_OPENABLE)
        choosePictureIntent.type = "image/*"
        if (choosePictureIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(choosePictureIntent, RC_CHOOSE_PICTURE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_TAKE_PHOTO -> {
                    ImageRescaleTask(currentPhotoPath, this).execute()
                }
                RC_CHOOSE_PICTURE -> {
                    if (data != null && data.data != null) {
                        val location = data.data!!.toString()
                        ImageRescaleTask(location, this).execute()
                    }

                }
            }
        }
    }

    inner class ImageRescaleTask(val localPath: String, val context : Context) : AsyncTask<Void, Void, Bitmap>() {
        override fun doInBackground(vararg p0: Void?): Bitmap? {
            // Reduces length and width by a factor (currently 2).
            val ratio = 2
            return BitmapUtils.rotateAndScaleByRatio(context, localPath, ratio)
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            storageAdd(localPath, bitmap)
        }
    }

    private fun storageAdd(localPath: String, bitmap: Bitmap?) {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val id = Math.abs(Random.nextLong()).toString()
        var uploadTask = storageRef.child(id).putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRef.child(id).downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                currentPostObject.mediaRef = downloadUri.toString()
                postsRef.add(currentPostObject)

            }
        }
    }

}
