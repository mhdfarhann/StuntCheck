package com.farhan.tugasakhir.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.farhan.tugasakhir.data.model.Artikel
import com.farhan.tugasakhir.data.model.ArtikelData
import com.farhan.tugasakhir.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {


    private val TAG = HomeViewModel::class.simpleName

    val isUserLoggedIn : MutableLiveData<Boolean> = MutableLiveData()

    private val _artikels = mutableListOf<Artikel>()

    init {
        _artikels.addAll(ArtikelData.artikels)
    }

    val artikels: List<Artikel>
        get() = _artikels


    fun logout(navController: NavController){
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign outSucces")
                navController.navigate(Screen.LoginScreen.route)
            } else {
                Log.d(TAG, "Inside sign out is not complete")
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun checkForActiveSession(){

        if (FirebaseAuth.getInstance().currentUser !=  null){

            Log.d(TAG, "valid session" )
            isUserLoggedIn.value = true

        }else{
            Log.d(TAG, "user is not logged in")
            isUserLoggedIn.value = false
        }

    }

}