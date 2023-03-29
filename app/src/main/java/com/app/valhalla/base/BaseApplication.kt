package com.app.valhalla.base

import android.app.Application
import com.app.valhalla.data.di.dataModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // declare the modules to use
            androidContext(this@BaseApplication)
            modules(dataModules)
        }
    }
}