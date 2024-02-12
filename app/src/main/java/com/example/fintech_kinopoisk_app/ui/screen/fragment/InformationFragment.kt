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
import com.example.fintech_kinopoisk_app.databinding.FragmentInformationalBinding
import com.example.fintech_kinopoisk_app.ui.screen.activity.MainActivity
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide


class InformationFragment : Fragment(R.layout.fragment_informational) {

    private var binding: FragmentInformationalBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInformationalBinding.bind(view)
        (requireContext() as MainActivity).changeBtnNavVisibility(false)

        val filmId = arguments?.getString("filmId", null) ?: return

        binding?.apply {
            val repository = KinopoiskApiRepositoryImpl()

            if (isNetworkAvailable()) {

                lifecycleScope.launch {
                    runCatching {
                        repository.getMovieById(
                            BuildConfig.KINOPOISK_API_KEY,
                            filmId.toInt()
                        )
                    }.onSuccess {
                        if (it == null) {
                            loaderImageInformational.visibility = View.VISIBLE
                            changeImageLoadingState()
                        } else {
                            loaderImageInformational.visibility = View.GONE
                            changeImageLoadingState()
                        }
                        tvCountryMovieInfo.text = it?.name
                        tvDescriptionMovieInfo.text = it?.description
                        tvGenreMovieInfo.text = it?.genres!![0].genre
                        tvCountryMovieInfo.text = it?.countries!![0].country
                        val posterUrl = it?.posterUrl
                        Glide.with(requireContext())
                            .load(posterUrl)
                            .into(imageMovieInfo)

                        infoBackButton.setOnClickListener {
                            findNavController().popBackStack()
                        }
                    }.onFailure {
                        val btmSheetDialogNetworkErrorFragment =
                            BtmSheetDialogNetworkErrorFragment()

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
            if (loaderImageInformational.visibility == View.VISIBLE) {
                loaderImageInformational.visibility = View.VISIBLE
                layoutContentMovieInfo.visibility = View.GONE
            } else {
                layoutContentMovieInfo.visibility = View.VISIBLE
                loaderImageInformational.visibility = View.GONE
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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