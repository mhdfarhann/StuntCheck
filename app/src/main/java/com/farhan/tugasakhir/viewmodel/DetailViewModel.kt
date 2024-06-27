package com.farhan.tugasakhir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farhan.tugasakhir.data.model.ArtikelRepository
import com.farhan.tugasakhir.uistate.UIState
import com.farhan.tugasakhir.data.model.Artikel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: ArtikelRepository
):ViewModel(){

    private val _uiState: MutableStateFlow<UIState<Artikel>> =
        MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState<Artikel>>
        get() = _uiState

    fun getArtikelById(id: Int) = viewModelScope.launch {
        _uiState.value = UIState.Loading
        _uiState.value = UIState.Success(repository.getArtikelById(id))
    }
}