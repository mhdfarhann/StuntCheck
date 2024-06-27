package com.farhan.tugasakhir.data.model

import com.google.firebase.firestore.DocumentSnapshot

data class Child(
    var name: String? = null,
    var gender: String? = null,
    var birthDate: String? = null,
    var height: Double = 0.0,
    var weight: Double = 0.0,
    var result: String? = null,
    var resultText: String? = null,
    var lastUpdated: Long? = null,
    var recommendation: String? = null,
    var growthData: List<GrowthData>? = emptyList(),
) {
    // Konstruktor tanpa argumen diperlukan untuk Firebase Firestore
    constructor() : this("", "", "")

    companion object {
        // Fungsi ekstensi untuk mengonversi DocumentSnapshot menjadi objek Child
        fun DocumentSnapshot.toChild(): Child? {
            return try {
                val name = getString("name")
                val gender = getString("gender")
                val birthDate = getString("birthDate")
                Child(name, gender, birthDate)
            } catch (e: Exception) {
                null
            }
        }
    }
}

data class GrowthData(
    val date: Long = System.currentTimeMillis(),
    val height: Double,
){
    constructor() : this(0, 0.0)
}