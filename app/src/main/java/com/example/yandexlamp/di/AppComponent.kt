package com.example.yandexlamp.di

import android.app.Application
import com.example.yandexlamp.di.viewModel.ViewModelModule
import com.example.yandexlamp.presenter.MainFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Component(
    modules = [AppModule::class]
)
@Singleton
abstract class AppComponent{
    abstract fun inject(fragment: MainFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
}

@Module(
    includes = [
        NetworkModule::class,
        AppBindsModule::class,
        ViewModelModule::class,
    ]
)
class AppModule