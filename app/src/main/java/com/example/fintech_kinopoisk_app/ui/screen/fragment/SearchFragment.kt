package com.example.fintech_kinopoisk_app.ui.screen.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fintech_kinopoisk_app.BuildConfig
import com.example.fintech_kinopoisk_app.R
import com.example.fintech_kinopoisk_app.data.api.repository.KinopoiskApiRepositoryImpl
import com.example.fintech_kinopoisk_app.databinding.FragmentSearchBinding
import com.example.fintech_kinopoisk_app.ui.recyclers.network.PopularSearchAdapter
import com.example.fintech_kinopoisk_app.ui.screen.activity.MainActivity
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var binding: FragmentSearchBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        (requireContext() as MainActivity).changeBtnNavVisibility(false)
        setTextWatchers()

        binding?.apply {

            searchBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun changeImageLoadingState() {
        binding?.apply {
            if (loaderImageSearchMovie.visibility == View.VISIBLE) {
                loaderImageSearchMovie.visibility = View.VISIBLE
                recyclerViewSearchMovie.visibility = View.GONE
            } else {
                recyclerViewSearchMovie.visibility = View.VISIBLE
                loaderImageSearchMovie.visibility = View.GONE
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

    private fun setTextWatchers() {

        val textWatcherEmpty: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {}
        }

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkAddress(s)
            }

            override fun afterTextChanged(s: Editable) {
            }
        }

        binding?.apply {
            editSearchMovie.addTextChangedListener(textWatcher)
        }
    }

    private fun checkAddress(s: CharSequence) {
        binding?.apply {
            val repository = KinopoiskApiRepositoryImpl()
            Log.e("TEST", s.toString())
            if (isNetworkAvailable()) {
                lifecycleScope.launch {
                    runCatching {
                        repository.getListMovieByKeyword(
                            tokenAuth = BuildConfig.KINOPOISK_API_KEY,
                            keyword = s.toString()
                        )
                    }.onSuccess {
                        if (it?.films == null) {
                            loaderImageSearchMovie.visibility = View.VISIBLE
                            changeImageLoadingState()
                        } else {
                            loaderImageSearchMovie.visibility = View.GONE
                            changeImageLoadingState()
                        }

                        recyclerViewSearchMovie.adapter = PopularSearchAdapter(
                            list = it?.films!!,
                            navController = findNavController(),
                            actionNav = R.id.action_searchFragment_to_informationFragment,
                            lifecycle = lifecycleScope,
                        )
                    }.onFailure {
//                        val btmSheetDialogNetworkErrorFragment =
//                            BtmSheetDialogNetworkErrorFragment()
//
//                        btmSheetDialogNetworkErrorFragment.show(
//                            (context as AppCompatActivity).supportFragmentManager,
//                            btmSheetDialogNetworkErrorFragment::class.java.simpleName
//                        )
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
}