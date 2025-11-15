package com.example.yandexlamp.presenter

import androidx.lifecycle.ViewModel
import com.example.yandexlamp.domain.GetLampUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getLampUseCase: GetLampUseCase
): ViewModel() {


}