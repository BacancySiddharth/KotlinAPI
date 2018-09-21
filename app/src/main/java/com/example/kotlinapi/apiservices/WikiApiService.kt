package com.example.kotlinapi.apiservices

import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Query

/**
 * Created by siddharth on 28/5/18.
 */
interface WikiApiService {

    @GET("categories.json")
    fun hitCountCheck(): Observable<JSONObject>

    @GET("categories.json")
    fun hitCountCheckRectro(): Call<JsonElement>

    @GET("top-headlines")
    fun getTopHeadlines(@Query("country") country: String, @Query("category") category: String): Call<JsonElement>


    companion object {
        fun create(): WikiApiService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl("http://quotes.rest/qod/")
                    .build()

            return retrofit.create(WikiApiService::class.java)
        }


    }
//    https://newsapi.org/v2/top-headlines
}