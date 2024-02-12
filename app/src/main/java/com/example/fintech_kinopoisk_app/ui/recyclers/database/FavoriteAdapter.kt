package com.example.fintech_kinopoisk_app.ui.recyclers.database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fintech_kinopoisk_app.data.db.entity.MovieEntity
import com.example.fintech_kinopoisk_app.databinding.ItemRecyclerBinding
import com.example.fintech_kinopoisk_app.ui.recyclers.network.PopularSearchHolder

class FavoriteAdapter(
    private val list: List<MovieEntity>,
    private val navController: NavController,
    private val actionNav: Int,
    private val lifecycle: LifecycleCoroutineScope
):RecyclerView.Adapter<FavoriteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder =
        FavoriteHolder(
            ItemRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            navController,
            actionNav,
            lifecycle
        )


    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bind(list, position)
    }
}