package com.example.yandexlamp.data


import com.example.yandexlamp.data.model.BrightnessLevel
import com.example.yandexlamp.data.model.ColorInfo
import javax.inject.Inject

interface LampRepository {
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

class LampRepositoryImpl @Inject constructor(
    private val service: LampService
): LampRepository {

    override suspend fun getBrightnessLevel(): BrightnessLevel? {
        val response =  service.getBrightnessLevel()

        return if (response.isSuccessful)
            response.body()
        else null
    }

    override suspend fun setBrightnessLevel(level: Int): Boolean {
        val response = service.setBrightnessLevel(level)

        return response.isSuccessful
    }

    override suspend fun getCurrentLevel(): Int? {
        val response =  service.getCurrentLevel()

        return if (response.isSuccessful)
            response.body()
        else 0
    }

    override suspend fun getColors(): List<ColorInfo>? {
        val response =  service.getColors()

        return if (response.isSuccessful)
            response.body()
        else null
    }

    override suspend fun setColor(color: String): Boolean {
        val response =  service.setColor(color)

        return response.isSuccessful
    }

    override suspend fun getCurrentColor(): ColorInfo? {
        val response =  service.getCurrentColor()

        return if (response.isSuccessful)
            response.body()
        else null
    }

    override suspend fun getColorNamesOnly(): List<String>?{
        val response =  service.getColorNamesOnly()

        return if (response.isSuccessful)
            response.body()
        else null
    }

    override suspend fun getState(): Boolean? {
        val response =  service.getState()

        return if (response.isSuccessful)
            response.body()
        else null
    }

    override suspend fun turnOn(): Boolean {
        val response =  service.turnOn()

        return response.isSuccessful
    }

    override suspend fun turnOff(): Boolean {
        val response =  service.turnOff()

        return response.isSuccessful
    }
}
