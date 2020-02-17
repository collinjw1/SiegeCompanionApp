package edu.rosehulman.collinjw.siegecompanionapp

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.fragment_submit_tip_page.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SubmitTipPage : Fragment() {
    private var listener: SubmitFragmentInteractionListener? = null
    private lateinit var rootView: View
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_submit_tip_page, container, false)
        rootView.upload_button.setOnClickListener {
            listener?.onPictureButtonPressed()
        }
        return rootView
    }

//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            // Ensure that there's a camera activity to handle the intent
//            val packageManager = (activity as MainActivity).packageManager
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        context!!,
//                        "edu.rosehulman.collinjw.siegecompanionapp",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
//                }
//            }
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            galleryAddPic()
//        }
//    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = (activity as MainActivity).getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
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
        MediaScannerConnection.scanFile(context,
            arrayOf(currentPhotoPath),
            null
        ) { path, uri ->
            if (uri != null) {
                Log.d(Constants.SUBMIT, "Scan successful. Path: $path, URI: $uri")
            } else {
                Log.e(Constants.SUBMIT, "Scan Failed")
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SubmitFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface SubmitFragmentInteractionListener {
        fun onPictureButtonPressed()
    }
}
