package com.farhan.tugasakhir.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileScreenViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> get() = _profileImageUrl

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _isProfileImageUpdated = MutableLiveData<Boolean?>(null)
    val isProfileImageUpdated: LiveData<Boolean?> get() = _isProfileImageUpdated

    private val _isPasswordChanged = MutableLiveData<Boolean?>(null)
    val isPasswordChanged: LiveData<Boolean?> get() = _isPasswordChanged

    private val _passwordChangeError = MutableLiveData<String?>()
    val passwordChangeError: LiveData<String?> get() = _passwordChangeError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        val userId = auth.currentUser?.uid
        userId?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        _userName.value = document.getString("name") ?: "User"
                        _profileImageUrl.value = document.getString("profileImageUrl") ?: ""
                        _email.value = document.getString("email") ?: "user@example.com"
                    } else {
                        // Handle the case when document is not found
                    }
                }
                .addOnFailureListener {
                    // Handle the error
                }
        }
    }

    fun updateProfileImage(uri: Uri) {
        _isLoading.value = true
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("profile_images/$userId.jpg")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    db.collection("users").document(userId)
                        .update("profileImageUrl", downloadUri.toString())
                        .addOnSuccessListener {
                            _profileImageUrl.value = downloadUri.toString()
                            _isProfileImageUpdated.value = true
                            _isLoading.value = false
                        }
                        .addOnFailureListener { e ->
                            _isProfileImageUpdated.value = false
                            _isLoading.value = false
                            Log.w("ProfileViewModel", "Error updating profile image", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                _isProfileImageUpdated.value = false
                _isLoading.value = false
                Log.w("ProfileViewModel", "Error uploading profile image", e)
            }
    }

    fun changePassword(newPassword: String, confirmPassword: String) {
        if (newPassword != confirmPassword) {
            _isPasswordChanged.value = false
            _passwordChangeError.value = "Passwords do not match"
            return
        }
        if (newPassword.length < 8) {
            _isPasswordChanged.value = false
            _passwordChangeError.value = "Password must be at least 8 characters long"
            return
        }

        val user = auth.currentUser
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isPasswordChanged.value = true
                    _passwordChangeError.value = null
                } else {
                    _isPasswordChanged.value = false
                    _passwordChangeError.value = task.exception?.message
                }
            }
        } else {
            _isPasswordChanged.value = false
            _passwordChangeError.value = "User is not authenticated"
        }
    }

    fun updateUsername(newUsername: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .update("name", newUsername)
            .addOnSuccessListener {
                _userName.value = newUsername
            }
            .addOnFailureListener { e ->
                Log.w("ProfileViewModel", "Error updating username", e)
            }
    }

    fun resetProfileImageUpdateState() {
        _isProfileImageUpdated.value = null
    }

    fun resetPasswordChangeState() {
        _isPasswordChanged.value = null
        _passwordChangeError.value = null
    }
}


