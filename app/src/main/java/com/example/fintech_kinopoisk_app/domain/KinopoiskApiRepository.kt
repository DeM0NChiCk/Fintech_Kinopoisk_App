package com.example.fintech_kinopoisk_app.domain

import com.example.fintech_kinopoisk_app.data.api.model.response.GetListMovieResponse
import com.example.fintech_kinopoisk_app.data.api.model.response.MovieResponse

interface KinopoiskApiRepository {

    suspend fun getListPopularMovie(tokenAuth: String, type: String): GetListMovieResponse?

    suspend fun getListMovieByKeyword(tokenAuth: String, keyword: String): GetListMovieResponse?

    suspend fun getMovieById(tokenAuth: String, id: Int): MovieResponse?

}