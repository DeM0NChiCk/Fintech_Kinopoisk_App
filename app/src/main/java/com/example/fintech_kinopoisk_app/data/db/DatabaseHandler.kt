package com.example.fintech_kinopoisk_app.data.db

import android.content.Context
import androidx.room.Room

object DatabaseHandler {
    const val databaseVersion: Int = 2

    var roomDatabase: InceptionDatabase? = null

    fun dbInitialize(appContext: Context) {
        if (roomDatabase == null) {
            roomDatabase =
                Room.databaseBuilder(appContext,
                    InceptionDatabase::class.java,
                    "movie_data")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}