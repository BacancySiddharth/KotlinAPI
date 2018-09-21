package com.example.kotlinapi.apiservices

import com.example.kotlinapi.model.NewsApiRes
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by siddharth on 28/5/18.
 */
interface NewsApiService {

//    https://timesofindia.indiatimes.com/rssfeeds/1898055.cms

    @POST("top-headlines")
    fun hitCountCheckRectro(@Query("country") country: String, @Query("category") category: String): Call<JsonElement>

    @GET("top-headlines")
    fun getTopHeadlines(@Query("country") country: String, @Query("category") category: String, @Query("apiKey") apiKey: String): Call<NewsApiRes>

    @GET("rssfeeds/-2128936835.cms")
    fun getBuissness(): Call<String>

    @GET("rssfeeds/296589292.cms")
    fun getWorldsNews(): Call<String>

    @GET("rssfeedstopstories.cms")
    fun getEconomics(): Call<String>

    @GET("{path}")
    fun getSimpleAllRSSNews(@Path("path") path: String): Call<String>


    companion object {
        fun create(): NewsApiService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl("https://newsapi.org/v2/")
                    .build()

            return retrofit.create(NewsApiService::class.java)
        }

        fun newsRss(): NewsApiService {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl("https://timesofindia.indiatimes.com/")
                    .build()

            return retrofit.create(NewsApiService::class.java)
        }

        fun economicRssNews(): NewsApiService {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl("https://economictimes.indiatimes.com/")
                    .build()

            return retrofit.create(NewsApiService::class.java)
        }

        fun loadFeedFromUrl(url: String): NewsApiService {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(url)
                    .build()

            return retrofit.create(NewsApiService::class.java)
        }

    }

//    https://newsapi.org/v2/top-headlines
}