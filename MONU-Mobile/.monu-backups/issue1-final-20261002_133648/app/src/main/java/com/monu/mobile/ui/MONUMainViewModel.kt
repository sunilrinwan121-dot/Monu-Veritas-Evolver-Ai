package com.monu.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MONUUiState {
    object Idle : MONUUiState()
    object Loading : MONUUiState()
    data class Success(val data: String) : MONUUiState()
    data class Error(val message: String) : MONUUiState()
}

class MONUMainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MONUUiState>(MONUUiState.Idle)
    val uiState: StateFlow<MONUUiState> = _uiState.asStateFlow()

    fun setSuccessState(data: String) {
        viewModelScope.launch {
            _uiState.value = MONUUiState.Success(data)
        }
    }

    fun setErrorState(message: String) {
        viewModelScope.launch {
            _uiState.value = MONUUiState.Error(message)
        }
    }

    fun setLoading() {
        _uiState.value = MONUUiState.Loading
    }

    fun resetState() {
        _uiState.value = MONUUiState.Idle
    }
}
