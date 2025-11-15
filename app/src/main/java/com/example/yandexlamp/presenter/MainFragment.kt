package com.example.yandexlamp.presenter

import androidx.fragment.app.Fragment
import com.example.yandexlamp.R
import com.example.yandexlamp.databinding.MainFragmentBinding
import dev.androidbroadcast.vbpd.viewBinding

class MainFragment: Fragment(R.layout.main_fragment) {
    private val binding: MainFragmentBinding by viewBinding(
        MainFragmentBinding::bind
    )
}