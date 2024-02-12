package com.example.fintech_kinopoisk_app.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    indices = [Index("filmId", unique = true)]
)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val filmId: Int,
    val name: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val year: Int,
    val genre: String,
    val country: String,
    val isFavorite: Boolean
)