package com.example.fintech_kinopoisk_app.ui.screen.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fintech_kinopoisk_app.BuildConfig
import com.example.fintech_kinopoisk_app.R
import com.example.fintech_kinopoisk_app.data.api.repository.KinopoiskApiRepositoryImpl
import com.example.fintech_kinopoisk_app.databinding.FragmentPopularBinding
import com.example.fintech_kinopoisk_app.ui.recyclers.network.PopularSearchAdapter
import com.example.fintech_kinopoisk_app.ui.screen.activity.MainActivity
import kotlinx.coroutines.launch

class PopularFragment: Fragment(R.layout.fragment_popular) {

    private var binding: FragmentPopularBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPopularBinding.bind(view)
        (requireContext() as MainActivity).changeBtnNavVisibility(true)

        binding?.apply {

            iconSearchPopular.setOnClickListener {
                findNavController().navigate(R.id.action_popularFragment_to_searchFragment)
            }

            val repository = KinopoiskApiRepositoryImpl()
            if (isNetworkAvailable()) {
                lifecycleScope.launch {
                    runCatching {
                        repository.getListPopularMovie(
                            tokenAuth = BuildConfig.KINOPOISK_API_KEY,
                            type = "TOP_100_POPULAR_FILMS"
                        )
                    }.onSuccess {
                        if (it?.films == null) {
                            loaderImagePopularMovie.visibility = View.VISIBLE
                            changeImageLoadingState()
                        } else {
                            loaderImagePopularMovie.visibility = View.GONE
                            changeImageLoadingState()
                        }

                        recyclerViewPopularMovie.adapter = PopularSearchAdapter(
                            list = it?.films!!,
                            navController = findNavController(),
                            actionNav = R.id.action_popularFragment_to_informationFragment,
                            lifecycle = lifecycleScope,
                        )
                    }.onFailure {
                        val btmSheetDialogNetworkErrorFragment = BtmSheetDialogNetworkErrorFragment()

                        btmSheetDialogNetworkErrorFragment.show(
                            (context as AppCompatActivity).supportFragmentManager,
                            btmSheetDialogNetworkErrorFragment::class.java.simpleName
                        )
                    }
                }
            } else {
                val btmSheetDialogNetworkErrorFragment = BtmSheetDialogNetworkErrorFragment()

                btmSheetDialogNetworkErrorFragment.show(
                    (context as AppCompatActivity).supportFragmentManager,
                    btmSheetDialogNetworkErrorFragment::class.java.simpleName
                )
            }

        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun changeImageLoadingState() {
        binding?.apply {
            if (loaderImagePopularMovie.visibility == View.VISIBLE) {
                loaderImagePopularMovie.visibility = View.VISIBLE
                recyclerViewPopularMovie.visibility = View.GONE
            } else {
                recyclerViewPopularMovie.visibility = View.VISIBLE
                loaderImagePopularMovie.visibility = View.GONE
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

}