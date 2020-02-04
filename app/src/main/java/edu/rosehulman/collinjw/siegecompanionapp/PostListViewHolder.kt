package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.directory_row_view.view.*
import kotlinx.android.synthetic.main.post_row_view.view.*

class PostListViewHolder : RecyclerView.ViewHolder {

    val postTitleTextView: TextView = itemView.post_title_text_view
    var context: Context

    constructor(itemView: View, adapter: PostListAdapter, context: Context): super(itemView) {
        this.context = context
        itemView.setOnClickListener {
            //adapter.onPhotoSelected(adapterPosition)
        }
    }

    fun bind(post: PostObject) {
        postTitleTextView.text = post.title

    }

}