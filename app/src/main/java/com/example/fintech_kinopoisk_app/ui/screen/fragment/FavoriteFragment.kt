package com.example.fintech_kinopoisk_app.ui.screen.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fintech_kinopoisk_app.R
import com.example.fintech_kinopoisk_app.data.db.DatabaseHandler
import com.example.fintech_kinopoisk_app.data.db.entity.MovieEntity
import com.example.fintech_kinopoisk_app.databinding.FragmentFavoriteBinding
import com.example.fintech_kinopoisk_app.ui.recyclers.database.FavoriteAdapter
import com.example.fintech_kinopoisk_app.ui.screen.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var binding: FragmentFavoriteBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        (requireContext() as MainActivity).changeBtnNavVisibility(true)

        binding?.apply {

            iconSearchFavorite.setOnClickListener {
                findNavController().navigate(R.id.action_favoriteFragment_to_searchFragment)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val list = DatabaseHandler.roomDatabase?.getMovieDao()?.getAll()
                val resultList = mutableListOf<MovieEntity>()
                for (s in list!!) {
                    if (s.isFavorite == true) {
                        resultList.add(s)
                    }
                }

                recyclerViewFavoriteMovie.adapter = FavoriteAdapter(
                    list = resultList,
                    navController = findNavController(),
                    actionNav = R.id.action_favoriteFragment_to_informationFragment,
                    lifecycle = lifecycleScope
                )
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}