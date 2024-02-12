package com.example.fintech_kinopoisk_app.ui.recyclers.network

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fintech_kinopoisk_app.BuildConfig
import com.example.fintech_kinopoisk_app.data.api.model.response.MovieResponse
import com.example.fintech_kinopoisk_app.data.api.repository.KinopoiskApiRepositoryImpl
import com.example.fintech_kinopoisk_app.data.db.DatabaseHandler
import com.example.fintech_kinopoisk_app.data.db.entity.MovieEntity
import com.example.fintech_kinopoisk_app.databinding.ItemRecyclerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopularSearchHolder(
    private val binding: ItemRecyclerBinding,
    private val navController: NavController,
    private val actionNav: Int,
    private val lifecycle: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        list: List<MovieResponse>,
        position: Int
    ) {
        binding.apply {

            val repository = KinopoiskApiRepositoryImpl()

            lifecycle.launch(Dispatchers.IO) {
                val isFavorite = try {
                    val filmById = DatabaseHandler.roomDatabase?.getMovieDao()?.findByFilmId(
                        list[position].id
                    )
                    filmById?.isFavorite == true
                } catch (ex: SQLiteConstraintException) {
                    false
                }

                withContext(Dispatchers.Main) {
                    if (!isFavorite) {
                        imageFavoriteBorder.visibility = View.VISIBLE
                        imageFavorite.visibility = View.INVISIBLE
                        return@withContext
                    }
                    imageFavorite.visibility = View.VISIBLE
                    imageFavoriteBorder.visibility = View.INVISIBLE
                    return@withContext
                }
            }

            itemTvTitleMovie.text = list[position].name
            itemTvYearMovie.text = list[position].year.toString()
            val imageUrl = list[position].posterUrlPreview

            Glide.with(root.context)
                .load(imageUrl)
                .into(itemImageMovie)

            root.setOnClickListener {
                navController.navigate(
                    actionNav,
                    bundleOf("filmId" to list[position].id.toString())
                )
            }

            root.setOnLongClickListener {
                lifecycle.launch(Dispatchers.IO) {
                    val isAddMovie = try {
                        DatabaseHandler.roomDatabase?.getMovieDao()?.addMovie(
                            MovieEntity(
                                filmId = list[position].id,
                                name = list[position].name,
                                posterUrl = list[position].posterUrl,
                                posterUrlPreview = list[position].posterUrlPreview,
                                year = list[position].year,
                                genre = list[position].genres[0].genre,
                                country = list[position].countries[0].country,
                                isFavorite = true,
                            )
                        )
                        true
                    } catch (ex: SQLiteConstraintException) {
                        false
                    }

                    withContext(Dispatchers.Main) {
                        if (!isAddMovie) {
                            Log.e("DATABASE_ERROR", "НЕ удалось добавить данные")
                            return@withContext
                        }
                        return@withContext
                    }
                }
                imageFavoriteBorder.visibility = View.INVISIBLE
                imageFavorite.visibility = View.VISIBLE
                true
            }

        }
    }
}