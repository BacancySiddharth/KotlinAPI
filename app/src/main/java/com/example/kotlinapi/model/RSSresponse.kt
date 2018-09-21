package com.example.kotlinapi.model
import com.google.gson.annotations.SerializedName


/**
 * Created by siddharth on 19/6/18.
 */

data class RSSresponse(
    @SerializedName("rss") val rss: Rss
)

data class Rss(
    @SerializedName("version") val version: Int,
    @SerializedName("xmlns:dc") val xmlnsdc: String,
    @SerializedName("channel") val channel: Channel
)

data class Channel(
        @SerializedName("title") val title: String,
        @SerializedName("link") val link: String,
        @SerializedName("description") val description: String,
        @SerializedName("language") val language: String,
        @SerializedName("copyright") val copyright: String,
        @SerializedName("category") val category: String,
        @SerializedName("pubDate") val pubDate: String,
        @SerializedName("image") val image: Image,
        @SerializedName("item") val item: List<Item>
)

data class Image(
    @SerializedName("url") val url: String,
    @SerializedName("title") val title: String,
    @SerializedName("height") val height: String,
    @SerializedName("link") val link: String
)

data class Item(
        @SerializedName("title") val title: String,
        @SerializedName("link") val link: String,
        @SerializedName("description") val description: String,
        @SerializedName("pubDate") val pubDate: String,
        @SerializedName("comments") val comments: String,
        @SerializedName("image") var image: String,
        @SerializedName("media:content") val imagecontent: Image,
        @SerializedName("guid") val guid: Guid
)

data class Guid(
    @SerializedName("isPermaLink") val isPermaLink: Boolean,
    @SerializedName("content") val content: String
)