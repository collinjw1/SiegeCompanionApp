package edu.rosehulman.collinjw.siegecompanionapp.ui.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import edu.rosehulman.collinjw.siegecompanionapp.Constants
import edu.rosehulman.collinjw.siegecompanionapp.DirectoryFragment
import edu.rosehulman.collinjw.siegecompanionapp.MainActivity
import edu.rosehulman.collinjw.siegecompanionapp.R
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlinx.android.synthetic.main.fragment_home_page.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var listener: OnHomeListener? = null
    lateinit var rootView: View

    init {
//        listener =
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnHomeListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_home_page, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(this, Observer {
//            textView.text = it
//        })
        rootView.operators_button.setOnClickListener {
            listener?.onButtonSelected(Constants.OPERATORS_COLLECTION)
        }
        rootView.maps_button.setOnClickListener {
            listener?.onButtonSelected(Constants.MAPS_COLLECTION)
        }
        rootView.submitatip_button.setOnClickListener {
            listener?.onButtonSelected(Constants.SUBMIT)
        }
        rootView.highlightReel_button.setOnClickListener {
            listener?.onButtonSelected("highlightReel")
        }

        return rootView
    }

    interface OnHomeListener {
        // TODO: Update argument type and name
        fun onButtonSelected(s: String)
    }
}