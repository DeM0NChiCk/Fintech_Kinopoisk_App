package com.example.fintech_kinopoisk_app.ui.recyclers.database

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fintech_kinopoisk_app.data.db.DatabaseHandler
import com.example.fintech_kinopoisk_app.data.db.entity.MovieEntity
import com.example.fintech_kinopoisk_app.data.db.model.UpdateMovieDataModel
import com.example.fintech_kinopoisk_app.databinding.FragmentInformationalBinding
import com.example.fintech_kinopoisk_app.databinding.ItemRecyclerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteHolder(
    private val binding: ItemRecyclerBinding,
    private val navController: NavController,
    private val actionNav: Int,
    private val lifecycle: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        list: List<MovieEntity>, position: Int
    ) {
        binding.apply {


            itemTvTitleMovie.text = list[position].name
            itemTvYearMovie.text = list[position].year.toString()
            val imageUrl = list[position].posterUrlPreview

            Glide.with(root.context).load(imageUrl).into(itemImageMovie)
            imageFavoriteBorder.visibility = View.INVISIBLE
            imageFavorite.visibility = View.VISIBLE

            root.setOnClickListener {
                navController.navigate(
                    actionNav, bundleOf("filmId" to list[position].filmId.toString())
                )
            }

            root.setOnLongClickListener {
                lifecycle.launch(Dispatchers.IO) {
                    val isFavorite = try {
                        val filmById =
                            DatabaseHandler.roomDatabase?.getMovieDao()?.findByFilmId(
                                list[position].id
                            )
                        filmById?.isFavorite == true
                    } catch (ex: SQLiteConstraintException) {
                        false
                    }

                    withContext(Dispatchers.Main) {
                        if (isFavorite) {
                            imageFavoriteBorder.visibility = View.INVISIBLE
                            imageFavorite.visibility = View.VISIBLE
                            return@withContext
                        }
                        lifecycle.launch(Dispatchers.IO) {
                            DatabaseHandler.roomDatabase?.getMovieDao()
                                ?.updateFavoriteMovie(
                                    UpdateMovieDataModel(
                                        list[position].id,
                                        false
                                    )
                                )
                        }
                        imageFavorite.visibility = View.INVISIBLE
                        imageFavoriteBorder.visibility = View.VISIBLE
                        return@withContext
                    }

                }
                true

            }
        }
    }
}