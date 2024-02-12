package com.example.fintech_kinopoisk_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fintech_kinopoisk_app.data.db.dao.MovieDao
import com.example.fintech_kinopoisk_app.data.db.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = DatabaseHandler.databaseVersion
)
abstract class InceptionDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}