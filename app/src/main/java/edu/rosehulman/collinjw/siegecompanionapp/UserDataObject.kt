package edu.rosehulman.collinjw.siegecompanionapp

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserDataObject (
    val uid: String = "",
    var siegeUsername: String = ""
) : Parcelable {

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): UserDataObject {
            val po = snapshot.toObject(UserDataObject::class.java)!!
            return po
        }
    }
}