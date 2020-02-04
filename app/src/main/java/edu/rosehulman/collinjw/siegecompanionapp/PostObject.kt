package edu.rosehulman.collinjw.siegecompanionapp

import android.icu.text.CaseMap
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class PostObject(
    val title: String,
    val textContent: String,
    val mediaRef: String,
    val tags: Array<String>

): Parcelable {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null

    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"
        fun fromSnapshot(snapshot: DocumentSnapshot): PostObject {
            val po = snapshot.toObject(PostObject::class.java)!!
            po.id = snapshot.id
            return po
        }
    }
}