package com.example.store_product.data.starting

import android.app.Application
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


class Starting : Application() {
    override fun onCreate() {
        super.onCreate()

        val systemModule = module {
            single {
                androidContext()
                    .getSystemService(ConnectivityManager::class.java) as ConnectivityManager
            }
        }

        val serviceModule = module {
//            single<> {  }
        }

        val viewModelModule = module {
//            viewModel { }
        }

        instance = this

        //initializes the Koin dependency injection framework
        startKoin {
            androidLogger()
            androidContext(this@Starting)
            modules(
                listOf(
                    systemModule,
                    serviceModule,
                    viewModelModule
                )
            )
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    companion object {
        lateinit var instance: Starting
            private set
    }
}