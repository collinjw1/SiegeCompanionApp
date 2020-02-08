package edu.rosehulman.collinjw.siegecompanionapp.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import edu.rosehulman.collinjw.siegecompanionapp.R
import edu.rosehulman.collinjw.siegecompanionapp.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_gallery.view.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var listener: OnSearchListener? = null
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = rootView.findViewById(R.id.search_by_tip_header)
        galleryViewModel.text.observe(this, Observer {
            textView.text = it
        })
        rootView.search_button.setOnClickListener {
            listener?.onSearch(rootView.search_edit_text.text.toString())
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSearchListener {
        fun onSearch(seachTag: String)
    }
}