package com.farhan.tugasakhir.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farhan.tugasakhir.data.model.ArtikelRepository
import com.farhan.tugasakhir.uistate.UIState
import com.farhan.tugasakhir.data.model.Artikel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ArtikelListScreenViewModel (private val repository: ArtikelRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UIState<List<Artikel>>> = MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState<List<Artikel>>> get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    init {
        search("") // Load initial data
    }

    fun search(newQuery: String) = viewModelScope.launch {
        _query.value = newQuery
        repository.searchArtikel(_query.value)
            .catch {
                _uiState.value = UIState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UIState.Success(it)
            }
    }

}