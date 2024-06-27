package com.farhan.tugasakhir.data.di

import com.farhan.tugasakhir.data.model.ArtikelRepository

object Injection {
    fun provideRepository(): ArtikelRepository {
        return ArtikelRepository.getInstance()
    }
}