package com.example.yandexlamp.di

import com.example.yandexlamp.data.LampService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
object NetworkModule {

    @Provides
    fun provideLampService(): LampService = Retrofit.Builder()
        .baseUrl("http://192.168.0.100:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create()
}
