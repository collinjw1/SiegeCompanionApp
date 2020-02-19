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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_submit_tip_page.*
import kotlinx.android.synthetic.main.fragment_submit_tip_page.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SubmitTipPage : Fragment() {
    private var listener: SubmitFragmentInteractionListener? = null
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_submit_tip_page, container, false)
        rootView.upload_button.setOnClickListener {
            val mediaRef = ""
            val title = rootView.title_edit_text.text.toString()
            val textContent = rootView.content_edit_text.text.toString()
            var tagsText = rootView.tag_edit_text.text.toString()
            if (highlightCheckBox.isChecked) {
                tagsText= tagsText + ", " + Constants.HIGHLIGHT_REEL
            }
            Log.d("fuck your log", tagsText)
            val tagsArray = tagsText.split(", ")
            var newPost =  PostObject(title, textContent, mediaRef, tagsArray)
            listener?.onPictureButtonPressed(newPost)
        }
        return rootView
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
        fun onPictureButtonPressed(po: PostObject)
    }
}
