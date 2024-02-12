package com.example.fintech_kinopoisk_app.data.api.network

import com.example.fintech_kinopoisk_app.data.api.model.response.GetListMovieResponse
import com.example.fintech_kinopoisk_app.data.api.model.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApiService {

    @GET("v2.2/films/top")
    suspend fun getPopularsMovie(
        @Query("type") type: String,
        @Header("x-api-key") tokenAuth: String
    ): GetListMovieResponse

    @GET("v2.2/films/{id}")
    suspend fun getMovieById(
        @Path(value = "id", encoded = true) id: Int,
        @Header("x-api-key") tokenAuth: String
    ): MovieResponse

    @GET("v2.1/films/search-by-keyword")
    suspend fun getMovieByKeyword(
        @Query("keyword") keyword: String,
        @Header("x-api-key") tokenAuth: String
    ): GetListMovieResponse

}