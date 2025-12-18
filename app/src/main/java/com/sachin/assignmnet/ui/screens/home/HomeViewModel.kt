package com.sachin.assignmnet.ui.screens.home

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sachin.assignmnet.data.repository.HistoryRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HistoryRepository) : ViewModel() {

    private val _url = MutableStateFlow("")
    val url: StateFlow<String> = _url.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    sealed class UiEvent {
        data class NavigateToWebView(val url: String) : UiEvent()
        object NavigateToHistory : UiEvent()
    }

    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    fun onUrlChange(newUrl: String) {
        _url.value = newUrl
        if (_error.value != null) {
            _error.value = null
        }
    }

    fun onClearInput() {
        _url.value = ""
    }

    fun onOpenClick() {
        var currentUrl = _url.value.trim()
        
        if (currentUrl.isEmpty()) {
            _error.value = "Please enter a URL"
            return
        }

        // Automatic prepend https://
        if (!currentUrl.startsWith("http://") && !currentUrl.startsWith("https://")) {
            currentUrl = "https://$currentUrl"
        }

        if (validateUrl(currentUrl)) {
            viewModelScope.launch {
                repository.insert(currentUrl)
                _uiEvent.send(UiEvent.NavigateToWebView(currentUrl))
            }
        } else {
            _error.value = "Please enter a valid URL"
        }
    }

    private fun validateUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    fun onHistoryClick() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.NavigateToHistory)
        }
    }
}

class HomeViewModelFactory(private val repository: HistoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
