package com.example.newsaggregator.data.rss.dto

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("content", "http://search.yahoo.com/mrss/", "media")
data class ContentDto(
    val width: Int? = null,
    val url: String,
    val type: String? = null,
    val credit: CreditDto? = null
)