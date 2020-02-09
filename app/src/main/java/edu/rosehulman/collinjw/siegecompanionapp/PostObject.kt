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
data class PostObject(
    var title: String = "",
    var textContent: String = "",
    var mediaRef: String = "",
    var tags: List<String> = listOf()

): Parcelable {
    @get:Exclude var id = ""
    @ServerTimestamp var lastTouched: Timestamp? = null

    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"
        fun fromSnapshot(snapshot: DocumentSnapshot): PostObject {
            val po = snapshot.toObject(PostObject::class.java)!!
            return po
        }
    }
}