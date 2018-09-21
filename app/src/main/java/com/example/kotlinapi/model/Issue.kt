package com.example.kotlinapi.model

/**
 * Created by siddharth on 28/5/18.
 */
data class Issue(
        val url: String,
        val id: Long,
        val number: Long,
        val title: String,
        val state: String,
        val created_at: String,
        val body: String)