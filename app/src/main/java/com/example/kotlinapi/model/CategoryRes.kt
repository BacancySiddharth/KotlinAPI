package com.example.kotlinapi.model

/**
 * Created by siddharth on 30/5/18.
 */

data class CategoryRes(
        val success: Success,
        val contents: Contents
)

data class Success(
        val total: Int
)

data class Contents(
        val categories: Categories,
        val copyright: String
)

data class Categories(
        val inspire: String,
        val management: String,
        val sports: String,
        val life: String,
        val funny: String,
        val love: String,
        val art: String,
        val students: String
)