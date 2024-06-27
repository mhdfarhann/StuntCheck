package com.farhan.tugasakhir.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhan.tugasakhir.data.model.Child
import com.google.firebase.firestore.FirebaseFirestore

class AddScreenViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _isChildAdded = MutableLiveData<Boolean?>(null)
    val isChildAdded: LiveData<Boolean?> = _isChildAdded

    fun addChild(child: Child, userId: String) {
        val userChildrenCollection = db.collection("users").document(userId).collection("children")
        userChildrenCollection.add(child)
            .addOnSuccessListener {
                _isChildAdded.value = true
                Log.d(TAG, "Dokumen berhasil ditambahkan")
            }
            .addOnFailureListener { e ->
                _isChildAdded.value = false
                Log.w(TAG, "Error menambahkan dokumen", e)
            }
    }

    fun resetChildAddedState() {
        _isChildAdded.value = null
    }
}
