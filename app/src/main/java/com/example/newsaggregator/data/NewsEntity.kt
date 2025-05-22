package com.example.newsaggregator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey
    val guid: String,
    val title: String,
    val link: String,
    val pubDate: String,
    val description: String,
    val imgUrlLarge: String?,
    val author: String
)

