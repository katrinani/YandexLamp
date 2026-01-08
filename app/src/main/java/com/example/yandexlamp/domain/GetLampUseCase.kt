package com.example.yandexlamp.domain

import com.example.yandexlamp.data.LampRepository
import com.example.yandexlamp.data.model.BrightnessLevel
import com.example.yandexlamp.data.model.ColorInfo
import javax.inject.Inject

interface GetLampUseCase {
    // уровни яркости
    suspend fun getBrightnessLevel(): BrightnessLevel?
    suspend fun setBrightnessLevel(level: Int): Boolean
    suspend fun getCurrentLevel(): Int?

    // цвета
    suspend fun getColors(): List<ColorInfo>?
    suspend fun setColor(color: String): Boolean
    suspend fun getCurrentColor(): ColorInfo?
    suspend fun getColorNamesOnly(): List<String>?

    // вкл/выкл
    suspend fun getState(): Boolean?
    suspend fun turnOn(): Boolean
    suspend fun turnOff(): Boolean
}

class GetLampUseCaseImpl @Inject constructor(
    private val repository: LampRepository
): GetLampUseCase {

    override suspend fun getBrightnessLevel(): BrightnessLevel? =
        repository.getBrightnessLevel()

    override suspend fun setBrightnessLevel(level: Int): Boolean =
        repository.setBrightnessLevel(level)

    override suspend fun getCurrentLevel(): Int? =
        repository.getCurrentLevel()

    override suspend fun getColors(): List<ColorInfo>? =
        repository.getColors()

    override suspend fun setColor(color: String): Boolean =
        repository.setColor(color)

    override suspend fun getCurrentColor(): ColorInfo? =
        repository.getCurrentColor()

    override suspend fun getColorNamesOnly(): List<String>? =
        repository.getColorNamesOnly()

    override suspend fun getState(): Boolean? =
        repository.getState()

    override suspend fun turnOn(): Boolean =
        repository.turnOn()

    override suspend fun turnOff(): Boolean =
        repository.turnOff()
}

