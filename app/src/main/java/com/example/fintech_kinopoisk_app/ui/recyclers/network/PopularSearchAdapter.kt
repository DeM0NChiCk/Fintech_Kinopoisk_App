package com.example.fintech_kinopoisk_app.ui.recyclers.network

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fintech_kinopoisk_app.data.api.model.response.MovieResponse
import com.example.fintech_kinopoisk_app.databinding.ItemRecyclerBinding

class PopularSearchAdapter(
    private val list: List<MovieResponse>,
    private val navController: NavController,
    private val actionNav: Int,
    private val lifecycle: LifecycleCoroutineScope
):RecyclerView.Adapter<PopularSearchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularSearchHolder =
        PopularSearchHolder(
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


    override fun onBindViewHolder(holder: PopularSearchHolder, position: Int) {
        holder.bind(list, position)
    }
}