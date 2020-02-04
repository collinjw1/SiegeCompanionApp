package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.directory_row_view.view.*

class DirectoryViewHolder : RecyclerView.ViewHolder {

    val directoryTextView: TextView = itemView.directory_text_view
    lateinit var context: Context

    constructor(itemView: View, adapter: DirectoryAdapter, context: Context): super(itemView) {
        this.context = context
        itemView.setOnClickListener {
            adapter.listener?.onDirectorySelected(directoryTextView.text.toString())
        }
    }

    fun bind(directory: String) {
        directoryTextView.text = directory

    }

}