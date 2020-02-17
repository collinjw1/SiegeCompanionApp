package edu.rosehulman.collinjw.siegecompanionapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.service.autofill.UserData
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.collinjw.siegecompanionapp.ui.gallery.GalleryFragment
import edu.rosehulman.collinjw.siegecompanionapp.ui.home.HomeFragment
import com.firebase.ui.auth.AuthUI
import com.google.common.io.Resources
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.collinjw.siegecompanionapp.ui.tools.ToolsFragment
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_TAKE_PHOTO = 1

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
    lateinit var scUserData: UserDataObject
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
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
            scUserData = UserDataObject.fromSnapshot(it)
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
        // TODO: Create an AuthStateListener that passes the UID
        // to the MovieQuoteFragment if the user is logged in
        // and goes back to the Splash fragment otherwise.
        // See https://firebase.google.com/docs/auth/users#the_user_lifecycle
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

//    override fun onDirectorySelected(searchTag: String) {
//        val postFragment = PostListFragment(searchTag)
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.nav_host_fragment, postFragment)
//        ft.addToBackStack("detail2")
//        ft.commit()
//    }

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

    override fun getUD(): UserDataObject {
        return scUserData
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
// Create and launch sign-in intent
        startActivityForResult(loginIntent, RC_SIGN_IN)



        //updateNavDrawer()


    }

    override fun onPictureButtonPressed() {
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
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

    private fun galleryAddPic() {

    }

   fun notifyMediaStoreScanner(file: File) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(Constants.SUBMIT, "activity returned")

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            galleryAddPic()
        }
    }


//    fun runDirectory() {
//        val dirFragment = DirectoryFragment()
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.nav_host_fragment, dirFragment)
//        ft.addToBackStack("detail")
//        ft.commit()
//
//    }
}
