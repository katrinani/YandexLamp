package com.example.yandexlamp.data.model

data class BrightnessLevel(
    val max: Int,
    val min: Int,
    val precision: Int // шаг с которым можно передвигаться от мин к макс
)
