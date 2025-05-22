package com.example.newsaggregator.di

import android.content.Context
import com.example.newsaggregator.data.NewsAggregatorDao
import com.example.newsaggregator.data.NewsAggregatorDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ) : NewsAggregatorDatabase {
        return NewsAggregatorDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideNewsAggregatorDao(
        newsAggregatorDatabase: NewsAggregatorDatabase
    ) : NewsAggregatorDao {
        return newsAggregatorDatabase.newsAggregatorDao()
    }
}