package com.example.newsaggregator.ui.screens.DetailScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsaggregator.domain.NewsAggregatorRepository
import com.example.newsaggregator.ui.model.toUiModel
import com.example.newsaggregator.ui.screens.DetailScreen.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val aggregatorRepository: NewsAggregatorRepository
) : ViewModel() {

    private val _detailUiState = MutableStateFlow(DetailUiState())
    val newsUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    fun loadDetailNews(guid: String) {
        viewModelScope.launch {
            aggregatorRepository.getNewsByGuidFromCache(guid)
                .catch { e ->
                    Log.e("NewsScreenViewModel", "Ошибка загрузки новости", e)
                }
                .collect { newsEntity ->
                    val news = newsEntity?.toUiModel()
                    val imageUrl = news?.imageUrlLarge
                    _detailUiState.update { currentState ->
                        currentState.copy(
                            title = news?.title,
                            description = news?.description,
                            image = imageUrl
                        )
                    }
                }
        }
    }
}
