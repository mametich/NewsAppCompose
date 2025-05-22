package com.example.newsaggregator.data

import android.util.Log
import com.example.newsaggregator.data.rss.RssFeed
import com.example.newsaggregator.data.rss.dto.ItemDto
import com.example.newsaggregator.domain.NewsAggregatorRepository
import com.example.newsaggregator.ui.model.NewsUiModel
import com.example.newsaggregator.ui.model.toUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NewsAggregatorRepositoryImpl(
    private val rssFeed: RssFeed,
    private val newsAggregatorDao: NewsAggregatorDao
) : NewsAggregatorRepository {

    override suspend fun getNews(): Flow<List<NewsUiModel>> = flow {
        newsAggregatorDao.getAllNewsFromCache().collect { items ->
            if (items.isEmpty()) {
                val news = rssFeed.getRss().channel.items
                newsAggregatorDao.addNews(news.map { it.toEntity() })
                emit(news.map { it.toUiModel()})
            } else {
                emit(items.map { it.toUiModel() })
            }
        }
    }

    override fun getNewsByGuidFromCache(guid: String): Flow<NewsEntity?> {
        return newsAggregatorDao.getNewsByGuidFromCache(guid)
    }
}