package com.example.newsaggregator.ui.screens.mainscreen.state

import com.example.newsaggregator.ui.model.NewsUiModel

sealed class NewsState {
    data object Loading : NewsState()
    data class Ready(val data: List<NewsUiModel>) : NewsState()
    data class Error(val message: String) : NewsState()
}