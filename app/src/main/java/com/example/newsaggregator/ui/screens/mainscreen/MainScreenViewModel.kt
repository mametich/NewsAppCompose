package com.example.newsaggregator.ui.screens.mainscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsaggregator.domain.NewsAggregatorRepository
import com.example.newsaggregator.ui.screens.mainscreen.state.NewsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val aggregatorRepository: NewsAggregatorRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewsState>(NewsState.Loading)
    val state: StateFlow<NewsState> = _state.asStateFlow()

    var isDateSorted by mutableStateOf(false)
        private set

    var query by mutableStateOf("")
        private set

    fun onQueryChanged(newQuery: String) {
        query = newQuery
    }

    fun onSearch(query: String) {
        this.query = query
    }

    fun onMenuClicked() {
        isDateSorted = !isDateSorted
    }

    fun loadAllNews() {
        viewModelScope.launch {
            try {
                _state.value = NewsState.Loading
                aggregatorRepository.getNews().map { items ->
                    _state.value = NewsState.Ready(items)
                }.stateIn(this)
            } catch (e: Exception) {
                _state.value = NewsState.Error(e.message ?: "Unknown error")
            }
        }
    }
}