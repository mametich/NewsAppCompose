package com.example.newsaggregator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.newsaggregator.data.NewsEntity

@Dao
interface NewsAggregatorDao {

    @Insert(onConflict = REPLACE)
    suspend fun addNews(listOfNews: List<NewsEntity>)

    @Query("SELECT * FROM news")
    fun getAllNewsFromCache() : Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE guid=:guid LIMIT 1")
    fun getNewsByGuidFromCache(guid: String) : Flow<NewsEntity?>

    @Query("DELETE FROM news")
    suspend fun clearAllNews()

    @Query("SELECT * FROM news WHERE guid=:guid LIMIT 1")
    suspend fun getNewsEntityByGuid(guid: String): NewsEntity?

}