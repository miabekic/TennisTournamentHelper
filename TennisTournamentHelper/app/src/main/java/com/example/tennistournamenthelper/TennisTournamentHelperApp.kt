package com.example.tennistournamenthelper

import android.app.Application
import com.example.tennistournamenthelper.di.appModule
import com.example.tennistournamenthelper.di.repositoryModule
import com.example.tennistournamenthelper.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TennisTournamentHelperApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TennisTournamentHelperApp)
            modules(appModule, repositoryModule, viewModelModule)
        }
    }
}