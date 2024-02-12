package com.example.fintech_kinopoisk_app.ui.screen.fragment

import android.os.Bundle
import android.view.View
import com.example.fintech_kinopoisk_app.R
import com.example.fintech_kinopoisk_app.data.api.repository.KinopoiskApiRepositoryImpl
import com.example.fintech_kinopoisk_app.databinding.BtmSheetDialogNetworkErrorBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BtmSheetDialogNetworkErrorFragment :
    BottomSheetDialogFragment(R.layout.btm_sheet_dialog_network_error) {

    private var binding: BtmSheetDialogNetworkErrorBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BtmSheetDialogNetworkErrorBinding.bind(view)

        binding?.apply {

            btnRepeatNetwork.setOnClickListener {
                restartActivity()
                dismiss()
            }

        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun restartActivity() {
        val intent = activity?.intent
        activity?.finish()
        startActivity(intent)
    }

}

