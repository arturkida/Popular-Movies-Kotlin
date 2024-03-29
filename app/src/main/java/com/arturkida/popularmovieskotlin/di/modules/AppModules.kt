package com.arturkida.popularmovieskotlin.di.modules

import android.arch.persistence.room.Room
import com.arturkida.popularmovieskotlin.data.local.AppDatabase
import com.arturkida.popularmovieskotlin.data.local.MovieDao
import com.arturkida.popularmovieskotlin.data.local.MovieRepository
import com.arturkida.popularmovieskotlin.data.remote.ApiImpl
import com.arturkida.popularmovieskotlin.ui.MoviesViewModel
import com.arturkida.popularmovieskotlin.utils.Constants
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            Constants.MOVIES_DATABASE
        ).build()
    }
    single<MovieDao> {
        get<AppDatabase>().movieDao()
    }
    single<MovieRepository> {
        MovieRepository(get<MovieDao>())
    }
    single<ApiImpl> {
        ApiImpl()
    }
    viewModel<MoviesViewModel> {
        MoviesViewModel(get(), get())
    }
}