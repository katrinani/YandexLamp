package com.example.yandexlamp.data

import com.example.yandexlamp.data.model.BrightnessLevel
import com.example.yandexlamp.data.model.ColorInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LampService {
    // яркость
    @GET("brightness/")
    suspend fun getBrightnessLevel():
            Response<BrightnessLevel>

    @POST("brightness/")
    suspend fun setBrightnessLevel(
        @Query("level") level: Int
    ): Response<Boolean>

    @GET("brightness/current/")
    suspend fun getCurrentLevel():
            Response<Int>

    // цвета
    @GET("color/")
    suspend fun getColors():
            Response<List<ColorInfo>>

    @POST("color/")
    suspend fun setColor(
        @Query("color") color: String
    ): Response<Boolean>

    @GET("color/current/")
    suspend fun getCurrentColor():
            Response<ColorInfo>

    @GET("color/names_only/")
    suspend fun getColorNamesOnly():
            Response<List<String>>

    // вкл/выкл
    @GET("state/")
    suspend fun getState():
            Response<Boolean> // true if ON, false if OFF

    @POST("state/on/")
    suspend fun turnOn():
            Response<Boolean>

    @POST("state/off/")
    suspend fun turnOff():
            Response<Boolean>
}
