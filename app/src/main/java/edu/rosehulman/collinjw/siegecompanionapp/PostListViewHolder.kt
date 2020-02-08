package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.directory_row_view.view.*
import kotlinx.android.synthetic.main.post_row_view.view.*

class PostListViewHolder : RecyclerView.ViewHolder {

    val postTitleTextView: TextView = itemView.post_title_text_view
    val postContentTextView: TextView = itemView.post_content_text_view
    var visible: Boolean = false
    var context: Context
    lateinit var postContent: View

    constructor(itemView: View, adapter: PostListAdapter, context: Context): super(itemView) {
        this.context = context
        //this.postContent = itemView.post_image
        itemView.setOnClickListener {
            //adapter.onPhotoSelected(adapterPosition)
            if (!visible) {
                itemView.post_image.visibility = ImageView.VISIBLE
                itemView.post_content_text_view.visibility = ImageView.VISIBLE
                itemView.post_title_text_view.maxHeight = 1000
            } else {
                itemView.post_image.visibility = ImageView.GONE
                itemView.post_content_text_view.visibility = ImageView.GONE
                itemView.post_title_text_view.maxHeight = 200
            }
            visible = !visible
            //itemView.post_image.visibility = ImageView.VISIBLE
        }
    }

    fun bind(post: PostObject) {
        //if statement testing for image vs video, set postContent variable
        postTitleTextView.text = post.title
        postContentTextView.text = post.textContent
//        if (visible) {
//            //showContent()
//        } else {
//            //hideContent()
//        }

    }

    fun showContent(post: PostObject) {

    }

}