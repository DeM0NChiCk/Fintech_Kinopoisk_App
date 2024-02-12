package com.example.fintech_kinopoisk_app.data.api.repository

import com.example.fintech_kinopoisk_app.data.api.model.response.GetListMovieResponse
import com.example.fintech_kinopoisk_app.data.api.model.response.MovieResponse
import com.example.fintech_kinopoisk_app.data.api.network.KinopoiskService
import com.example.fintech_kinopoisk_app.domain.KinopoiskApiRepository
import kotlinx.coroutines.Dispatchers

class KinopoiskApiRepositoryImpl: KinopoiskApiRepository {
    override suspend fun getListPopularMovie(
        tokenAuth: String,
        type: String
    ): GetListMovieResponse? {
        Result
        return with(Dispatchers.IO){
            KinopoiskService.getInstance()?.getPopularsMovie(
                type = type,
                tokenAuth = tokenAuth
            )
        }
    }

    override suspend fun getListMovieByKeyword(
        tokenAuth: String,
        keyword: String
    ): GetListMovieResponse? {
        Result
        return with(Dispatchers.IO){
            KinopoiskService.getInstance()?.getMovieByKeyword(
                keyword = keyword,
                tokenAuth = tokenAuth
            )
        }
    }

    override suspend fun getMovieById(tokenAuth: String, id: Int): MovieResponse? {
        Result
        return with(Dispatchers.IO) {
            KinopoiskService.getInstance()?.getMovieById(
                id = id,
                tokenAuth = tokenAuth
            )
        }
    }

}