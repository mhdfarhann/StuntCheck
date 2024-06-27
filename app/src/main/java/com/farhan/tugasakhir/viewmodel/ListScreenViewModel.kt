package com.farhan.tugasakhir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhan.tugasakhir.data.model.Child
import com.farhan.tugasakhir.data.model.GrowthData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ListScreenViewModel : ViewModel() {
    val db = FirebaseFirestore.getInstance()

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var listenerRegistration: ListenerRegistration? = null

    // Mendengarkan perubahan data anak-anak berdasarkan ID pengguna
    fun startListening(userId: String) {
        _isLoading.value = true  // Mulai memuat data
        val userChildrenCollection = db.collection("users").document(userId).collection("children")

        listenerRegistration = userChildrenCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                _isLoading.value = false
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val childrenList = mutableListOf<Child>()
                for (document in snapshot.documents) {
                    val child = document.toObject(Child::class.java)
                    child?.let {
                        childrenList.add(it)
                    }
                }
                _children.value = childrenList
                _isLoading.value = false  // Selesai memuat data
            } else {
                _children.value = emptyList()
                _isLoading.value = false  // Selesai memuat data
            }
        }
    }

    // Menghentikan pendengar perubahan data saat ViewModel dihancurkan
    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

    // Mendapatkan data anak-anak berdasarkan ID pengguna (untuk CheckScreen)
    fun fetchChildren(userId: String) {
        _isLoading.value = true  // Mulai memuat data
        val userChildrenCollection = db.collection("users").document(userId).collection("children")
        userChildrenCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val childrenList = mutableListOf<Child>()
                for (document in querySnapshot.documents) {
                    val child = document.toObject(Child::class.java)
                    child?.let {
                        childrenList.add(it)
                    }
                }
                _children.value = childrenList
                _isLoading.value = false  // Selesai memuat data
            }
            .addOnFailureListener { e ->
                _isLoading.value = false  // Gagal memuat data
            }
    }

    fun updateChildData(userId: String, child: Child, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userChildrenCollection = db.collection("users").document(userId).collection("children")
        userChildrenCollection
            .whereEqualTo("name", child.name)
            .whereEqualTo("birthDate", child.birthDate)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    child.lastUpdated = System.currentTimeMillis()
                    document.reference.set(child)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                } else {
                    onFailure(Exception("Child not found"))
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateGrowthData(userId: String, child: Child, growthData: GrowthData, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userChildrenCollection = db.collection("users").document(userId).collection("children")
        userChildrenCollection
            .whereEqualTo("name", child.name)
            .whereEqualTo("birthDate", child.birthDate)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val updatedGrowthData = child.growthData?.toMutableList() ?: mutableListOf()
                    updatedGrowthData.add(growthData)
                    val updatedChild = child.copy(growthData = updatedGrowthData, lastUpdated = System.currentTimeMillis())
                    document.reference.set(updatedChild)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                } else {
                    onFailure(Exception("Child not found"))
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Metode untuk menghapus data anak dari Firebase (tidak berubah)
    fun deleteChild(userId: String, child: Child, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userChildrenCollection = db.collection("users").document(userId).collection("children")
        userChildrenCollection
            .whereEqualTo("name", child.name)
            .whereEqualTo("birthDate", child.birthDate)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    document.reference.delete()
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                } else {
                    onFailure(Exception("Child not found"))
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}

