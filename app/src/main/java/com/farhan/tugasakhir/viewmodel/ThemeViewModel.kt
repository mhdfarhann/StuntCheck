package com.farhan.tugasakhir.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    private val _isDarkMode = MutableLiveData<Boolean?>(null)  // Initialized to null
    val isDarkMode: LiveData<Boolean?> get() = _isDarkMode

    fun toggleDarkMode() {
        _isDarkMode.value = _isDarkMode.value != true
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }
}

