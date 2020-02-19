package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.directory_row_view.view.*
import kotlinx.android.synthetic.main.post_row_view.view.*

class PostListViewHolder : RecyclerView.ViewHolder {

    val postTitleTextView: TextView = itemView.post_title_text_view
    val postContentTextView: TextView = itemView.post_content_text_view
    var visible: Boolean = false
    var context: Context
    var firstRunCheck = true
    lateinit var rootView: View

    constructor(itemView: View, adapter: PostListAdapter, context: Context): super(itemView) {
        this.context = context
        rootView = itemView
        itemView.setOnClickListener {
            if (!visible) {
                itemView.post_image.visibility = ImageView.VISIBLE
                itemView.post_content_text_view.visibility = ImageView.VISIBLE
                itemView.post_title_text_view.maxHeight = 1000
                itemView.more_button.setImageResource(R.drawable.ic_remove_black_24dp)
            } else {
                itemView.post_image.visibility = ImageView.GONE
                itemView.post_content_text_view.visibility = ImageView.GONE
                itemView.post_title_text_view.maxHeight = 200
                itemView.more_button.setImageResource(R.drawable.ic_add_black_24dp)
            }
            visible = !visible
        }
    }

    fun bind(post: PostObject) {
        if (firstRunCheck) {
            Picasso.get()
                .load(post.mediaRef)
                .into(rootView.post_image)
            firstRunCheck = false
        }
        postTitleTextView.text = post.title
        postContentTextView.text = post.textContent

    }



}