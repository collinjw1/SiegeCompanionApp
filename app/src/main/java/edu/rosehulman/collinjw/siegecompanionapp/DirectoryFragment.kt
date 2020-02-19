package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DirectoryFragment(val path: String) : Fragment() {

    private var listener: OnDirectoryListener? = null
    lateinit var adapter: DirectoryAdapter
    var savedContext: OnDirectoryListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recyclerView = inflater.inflate(R.layout.fragment_directory, container, false) as RecyclerView
    adapter = DirectoryAdapter(context!!, path, savedContext!!)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.setHasFixedSize(true)
    return recyclerView
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDirectoryListener) {
            //adapter.giveListener(context)
            savedContext = context
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnDirectoryListener {
        fun onDirectorySelected(searchTag: String)
    }


}
