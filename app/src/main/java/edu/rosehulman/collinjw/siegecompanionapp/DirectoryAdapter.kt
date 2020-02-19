package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.collinjw.siegecompanionapp.ui.home.HomeFragment

class DirectoryAdapter(var context: Context, val path: String, val listener: DirectoryFragment.OnDirectoryListener) : RecyclerView.Adapter<DirectoryViewHolder>() {

    val directories = ArrayList<String>()
    val directoryRef = FirebaseFirestore
        .getInstance()
        .collection(path)

    init {


        directoryRef.addSnapshotListener{ snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
            if (exception != null) {
                return@addSnapshotListener
            }
            for (docChange in snapshot!!.documentChanges) {
                directories.add(directories.size, docChange.document.id)
                notifyItemInserted(0)
            }
        }

    }

    override fun getItemCount() = directories.size

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): DirectoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.directory_row_view, parent, false)
        return DirectoryViewHolder(view, this, context)
    }

    override fun onBindViewHolder(viewHolder: DirectoryViewHolder, index: Int) {
        viewHolder.bind(directories[index])

    }


}