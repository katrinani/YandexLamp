package com.example.yandexlamp.di


import com.example.yandexlamp.data.LampRepository
import com.example.yandexlamp.data.LampRepositoryImpl
import com.example.yandexlamp.domain.GetLampUseCase
import com.example.yandexlamp.domain.GetLampUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindsModule {
    @Binds
    fun bindLampRepository(impl: LampRepositoryImpl): LampRepository

    @Binds
    fun bindGetLampUseCase(
        impl: GetLampUseCaseImpl
    ): GetLampUseCase
}