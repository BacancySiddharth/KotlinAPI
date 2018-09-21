package com.example.kotlinapi.model

import com.example.kotlinapi.model.Article
import com.google.gson.annotations.SerializedName

data class NewsApiRes(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<Article>
)