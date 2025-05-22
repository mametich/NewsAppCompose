package com.example.newsaggregator.data.rss

import com.example.newsaggregator.data.rss.dto.ItemDto
import com.example.newsaggregator.data.rss.dto.RssDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import nl.adaptivity.xmlutil.serialization.XML
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface RssFeed {

    @GET("/{query}/rss")
    suspend fun getRss(
        @Path("query") query: String = "international"
    ): RssDto
}