package com.farhan.tugasakhir.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Tools {
    companion object {
        // Fungsi untuk mengonversi Long ke format waktu (String)
        fun convertLongToTime(timeInMillis: Long?): String {
            if (timeInMillis == null) {
                return ""
            }
            // Format waktu yang diinginkan, misalnya "dd-MM-yyyy HH:mm:ss"
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = Date(timeInMillis)
            return dateFormat.format(date)
        }
    }
}
