package edu.rosehulman.collinjw.siegecompanionapp.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Search by tip"
    }
    val text: LiveData<String> = _text
}