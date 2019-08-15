package com.arturkida.popularmovieskotlin.ui.home

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.arturkida.popularmovieskotlin.data.local.MovieRepository

class MoviesViewModelFactory (
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(repository) as T
    }
}