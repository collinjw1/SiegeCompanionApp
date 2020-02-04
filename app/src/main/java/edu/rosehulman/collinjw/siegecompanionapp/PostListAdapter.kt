package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class PostListAdapter(var context: Context, val searchTag: String) : RecyclerView.Adapter<PostListViewHolder>() {


    val posts = ArrayList<PostObject>()
    val postsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.POSTS_COLLECTION)

    init {


        postsRef
            .whereArrayContains("tags", searchTag)
            .addSnapshotListener{ snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
            if (exception != null) {
                return@addSnapshotListener
            }
            for (docChange in snapshot!!.documentChanges) {
                val post = PostObject.fromSnapshot(docChange.document)
                posts.add(posts.size, post)
                notifyItemInserted(posts.size)
            }
        }

    }

    override fun getItemCount() = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): PostListViewHolder {
        //Log.d(Constants.TAG, "Creating VH")
        val view = LayoutInflater.from(context).inflate(R.layout.post_row_view, parent, false)
        return PostListViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        holder.bind(posts[position])
    }
}