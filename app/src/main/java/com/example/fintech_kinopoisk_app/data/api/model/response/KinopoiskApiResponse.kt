package com.example.fintech_kinopoisk_app.data.api.model.response

import com.google.gson.annotations.SerializedName

data class GetListMovieResponse(
    @SerializedName("films")
    val films: List<MovieResponse>? = null
)

data class MovieResponse(
    @SerializedName("filmId")
    val id: Int,
    @SerializedName("nameRu")
    val name: String,
    @SerializedName("posterUrl")
    val posterUrl: String,
    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("genres")
    val genres: List<GenresResponse>,
    @SerializedName("countries")
    val countries: List<CountryResponse>,
    @SerializedName("description")
    val description: String

)

data class GenresResponse(
    @SerializedName("genre")
    val genre: String
)

data class CountryResponse(
    @SerializedName("country")
    val country: String
)