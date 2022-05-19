package com.c22ps305team.saferoute.ui.main.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapsViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is maps Fragment"
    }
    val text: LiveData<String> = _text
}