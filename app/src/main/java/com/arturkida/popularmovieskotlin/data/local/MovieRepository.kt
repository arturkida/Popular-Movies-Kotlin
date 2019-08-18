package com.arturkida.popularmovieskotlin.data.local

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.arturkida.popularmovieskotlin.model.Movie

class MovieRepository(private val movieDao: MovieDao?) {

    fun getMoviesFromDatabase() : LiveData<List<Movie>?>? = movieDao?.getAllMovies()

    fun addMovie(movie: Movie) {
        InsertMovieAsyncTask(movieDao).execute(movie)
    }

    fun deleteMovie(movie: Movie) {
        DeleteMovieAsyncTask(movieDao).execute(movie)
    }

    private class InsertMovieAsyncTask(private val movieDao: MovieDao?) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg params: Movie) {
            movieDao?.addMovie(params[0])
        }
    }

    private class DeleteMovieAsyncTask(private val movieDao: MovieDao?) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg params: Movie) {
            movieDao?.deleteMovie(params[0])
        }
    }
}