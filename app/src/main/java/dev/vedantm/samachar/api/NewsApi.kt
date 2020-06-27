package dev.vedantm.samachar.api

import dev.vedantm.samachar.models.NewsResponse
import dev.vedantm.samachar.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String="us",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY): Response<NewsResponse>

    @GET("v2/search")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY): Response<NewsResponse>

    }


