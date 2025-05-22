package com.example.newsaggregator.domain

import com.example.newsaggregator.data.NewsEntity
import com.example.newsaggregator.data.rss.dto.ItemDto
import com.example.newsaggregator.ui.model.NewsUiModel
import kotlinx.coroutines.flow.Flow

interface NewsAggregatorRepository {

    suspend fun getNews(): Flow<List<NewsUiModel>>

    fun getNewsByGuidFromCache(guid: String) : Flow<NewsEntity?>
}