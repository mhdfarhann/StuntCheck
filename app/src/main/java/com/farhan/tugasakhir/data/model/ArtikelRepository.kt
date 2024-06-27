package com.farhan.tugasakhir.data.model

import kotlinx.coroutines.flow.flow

class ArtikelRepository {
    private val artikels = mutableListOf<Artikel>()

    init {
        if (artikels.isEmpty()){
            ArtikelData.artikels.forEach {
                artikels.add(it)
            }
        }
    }

    fun searchArtikel(query : String) = flow {
        val data = artikels.filter {
            it.judul.contains(query, ignoreCase = true)
        }
        emit(data)
    }

    fun getArtikelById(artikelId : Int) : Artikel {
        return artikels.first{
            it.id == artikelId
        }

    }

    companion object {
        @Volatile
        private var instance: ArtikelRepository? = null

        fun getInstance(): ArtikelRepository =
            instance ?: synchronized(this) {
                ArtikelRepository().apply {
                    instance = this
                }
            }
    }
}