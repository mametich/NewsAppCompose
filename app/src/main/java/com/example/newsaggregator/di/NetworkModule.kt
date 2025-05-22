package com.example.newsaggregator.di


import com.example.newsaggregator.data.NewsAggregatorDao
import com.example.newsaggregator.data.NewsAggregatorRepositoryImpl
import com.example.newsaggregator.data.rss.RssFeed
import com.example.newsaggregator.domain.NewsAggregatorRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.adaptivity.xmlutil.serialization.XML
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsAggregatorApiService(
        okHttpClient: OkHttpClient
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.theguardian.com")
            .client(okHttpClient)
            .addConverterFactory(
                XML.asConverterFactory(
                    "application/xml; charset=UTF8".toMediaType()
                )
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit) : RssFeed {
        return retrofit.create(RssFeed::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsAggregatorRepository(
        rssFeed: RssFeed,
        newsAggregatorDao: NewsAggregatorDao
    ) : NewsAggregatorRepository {
        return NewsAggregatorRepositoryImpl(rssFeed, newsAggregatorDao)
    }
}