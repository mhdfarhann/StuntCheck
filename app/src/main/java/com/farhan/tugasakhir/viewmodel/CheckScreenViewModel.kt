package com.farhan.tugasakhir.viewmodel

import androidx.lifecycle.ViewModel
import com.farhan.tugasakhir.data.model.femaleStandards
import com.farhan.tugasakhir.data.model.maleStandards
class CheckScreenViewModel : ViewModel() {

    fun classifyChild(ageInMonths: Int, gender: String, height: Double, weight: Double): String {
        if (height < 0 || weight < 0) {
            return "Data tidak valid"
        }

        val standards = if (gender == "male") maleStandards else femaleStandards
        val standard = standards.find { it.ageInMonths == ageInMonths }
            ?: return "Data tidak ditemukan untuk umur ini."

        val heightZScore = calculateZScore(height, standard.medianHeight, standard.stdDevHeight)
        val weightZScore = calculateZScore(weight, standard.medianWeight, standard.stdDevWeight)

        val heightStatus = when {
            heightZScore < -3 -> "Sangat Pendek (Stunting)"
            heightZScore < -2 -> "Pendek"
            heightZScore <= 2 -> "Normal"
            else -> "Tinggi"
        }

        val weightStatus = when {
            weightZScore < -3 -> "Gizi Buruk"
            weightZScore < -2 -> "Gizi Kurang"
            weightZScore <= 2 -> "Gizi Baik"
            else -> "Gizi Lebih (Obesitas)"
        }

        return "Status Tinggi Badan: $heightStatus,\nStatus Berat Badan: $weightStatus"
    }

    private fun calculateZScore(value: Double, mean: Double, stdDev: Double): Double {
        return if (stdDev != 0.0) (value - mean) / stdDev else 0.0
    }
}