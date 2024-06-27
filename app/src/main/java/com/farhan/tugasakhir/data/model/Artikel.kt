package com.farhan.tugasakhir.data.model

data class Artikel(
    val id : Int,
    val judul : String,
    val imageUrl :String,
    val content : String,
    val hasTable: Boolean,
    val pdfUrl: String? = null,
    val hasPdf: Boolean = pdfUrl != null
)