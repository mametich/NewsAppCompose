package com.example.newsaggregator.data

import android.util.Log
import com.example.newsaggregator.data.rss.dto.ContentDto
import com.example.newsaggregator.data.rss.dto.ItemDto

fun ItemDto.toEntity(): NewsEntity {
    val imgUrlLarge = this.contents.findImageUrlLarge()
    return NewsEntity(
        guid = this.guid,
        title = this.title,
        link = this.link,
        pubDate = this.pubDate,
        description = this.description,
        imgUrlLarge = imgUrlLarge,
        author = this.dcCreator
    )
}



fun NewsEntity.toDto(): ItemDto =
    ItemDto(
        title = this.title,
        link = this.link,
        description = this.description,
        categories = emptyList(),
        pubDate = this.pubDate,
        guid = this.guid,
        contents = emptyList(),
        dcCreator = "",
        dcDate = ""
    )

fun List<ContentDto>.findImageUrlLarge(): String? =
    this.firstOrNull { it.width == 460 }?.url