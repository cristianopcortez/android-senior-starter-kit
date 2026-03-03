package br.com.ccortez.seniorstarterkitapplication.presentation.viewmodel

import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<T> : ViewModel() {
    protected val _uiState = MutableStateFlow<Resource<T>>(Resource.Loading)
    val uiState: StateFlow<Resource<T>> = _uiState.asStateFlow()
}