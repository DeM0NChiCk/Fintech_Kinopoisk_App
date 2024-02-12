package com.example.fintech_kinopoisk_app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fintech_kinopoisk_app.data.db.entity.MovieEntity
import com.example.fintech_kinopoisk_app.data.db.model.UpdateMovieDataModel

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: MovieEntity)

    @Update(entity = MovieEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun updateFavoriteMovie(favoriteMovie: UpdateMovieDataModel)

    @Query("SElECT * FROM movies WHERE filmId = :filmId")
    suspend fun findByFilmId(filmId: Int): MovieEntity

    @Query("SELECT * FROM movies")
    suspend fun getAll(): List<MovieEntity>

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteMovieByID(id: Int)
}