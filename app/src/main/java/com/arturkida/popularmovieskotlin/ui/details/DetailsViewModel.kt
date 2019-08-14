package com.arturkida.popularmovieskotlin.ui.details

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.arturkida.popularmovieskotlin.data.local.MovieRepository
import com.arturkida.popularmovieskotlin.model.Movie

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(application)

    fun addMovie(movie: Movie) = repository.addMovie(movie)

    fun deleteMovie(movie: Movie) = repository.deleteMovie(movie)
}
