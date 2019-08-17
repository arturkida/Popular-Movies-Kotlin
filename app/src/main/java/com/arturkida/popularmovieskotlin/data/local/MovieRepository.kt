package com.arturkida.popularmovieskotlin.data.local

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.arturkida.popularmovieskotlin.model.Movie
import com.arturkida.popularmovieskotlin.utils.Constants

class MovieRepository(context: Context?) {

    private val movieDao: MovieDao?
    private val allFavoriteMovies: LiveData<List<Movie>?>?

    init {
        val database = AppDatabase.getAppDatabase(context)
        movieDao = database?.movieDao()
        allFavoriteMovies = movieDao?.getAllMovies()
    }

    fun getMoviesFromDatabase() : LiveData<List<Movie>?>? = allFavoriteMovies

    fun addMovie(movie: Movie) {
        InsertMovieAsyncTask(movieDao).execute(movie)
    }

    fun deleteMovie(movie: Movie) {
        DeleteMovieAsyncTask(movieDao).execute(movie)
    }

    private class InsertMovieAsyncTask(private val movieDao: MovieDao?) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg params: Movie) {
            Log.i(Constants.LOG_INFO, "Adding movie to database")
            movieDao?.addMovie(params[0])
        }
    }

    private class DeleteMovieAsyncTask(private val movieDao: MovieDao?) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg params: Movie) {
            Log.i(Constants.LOG_INFO, "Removing movie from database")
            movieDao?.deleteMovie(params[0])
        }
    }
}