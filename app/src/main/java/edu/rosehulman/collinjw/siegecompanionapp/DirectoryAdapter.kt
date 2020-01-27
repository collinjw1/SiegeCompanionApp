package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DirectoryAdapter(var context: Context) : RecyclerView.Adapter<DirectoryViewHolder>() {

    val directories = ArrayList<String>()

    init {
        directories.add("Ash")
        directories.add("Sledge")
        directories.add("Thatcher")
        directories.add("Thermite")
    }

    override fun getItemCount() = directories.size

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): DirectoryViewHolder {
        //Log.d(Constants.TAG, "Creating VH")
        val view = LayoutInflater.from(context).inflate(R.layout.directory_row_view, parent, false)
        return DirectoryViewHolder(view, this, context)
    }

    override fun onBindViewHolder(viewHolder: DirectoryViewHolder, index: Int) {
        viewHolder.bind(directories[index])

    }
}