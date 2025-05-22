package com.example.newsaggregator.ui.model

import com.example.newsaggregator.data.NewsEntity
import com.example.newsaggregator.data.rss.dto.ItemDto
import java.text.SimpleDateFormat
import java.util.Locale

data class NewsUiModel(
    val guid: String,
    val title: String,
    val description: String,
    val pubDate: String,
    val link: String,
    val categories: List<String> = emptyList(),
    val author: String? = null,
    val imageUrlLarge: String?,
)


fun ItemDto.toUiModel(): NewsUiModel =
    NewsUiModel(
        guid = this.guid,
        title = this.title,
        description = this.description,
        imageUrlLarge = this.contents.firstOrNull()?.url ?: "",
        pubDate = this.pubDate,
        link = this.link,
        categories = this.categories.map { it.value },
        author = this.dcCreator
    )

fun NewsEntity.toUiModel() : NewsUiModel =
    NewsUiModel(
        guid = this.guid,
        title = this.title,
        link = this.link,
        pubDate = this.pubDate,
        description = this.description,
        imageUrlLarge = this.imgUrlLarge,
        author = this.author
    )

fun NewsUiModel.parseAndFormatDate(pubDate: String): String {
    val cleanDate = pubDate.removeSurrounding("<pubDate>", "</pubDate>")
    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    return try {
        val date = inputFormat.parse(cleanDate)
        outputFormat.format(date)
    } catch (e: Exception) {
        "Invalid date"
    }
}