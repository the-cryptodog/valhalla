package com.app.valhalla.data.di

import com.app.valhalla.data.MainDataSource
import com.app.valhalla.data.MainRepository
import com.app.valhalla.ui.drawlots.DrawLotsViewModel
import com.app.valhalla.ui.launch.LaunchViewModel
import com.app.valhalla.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModules = module {

    viewModel {
        MainViewModel(get())
    }
    viewModel {
        LaunchViewModel(get())
    }
    viewModel{
        DrawLotsViewModel(get())
    }
    single {
        MainRepository(get())
    }
    single {
        MainDataSource()
    }
}