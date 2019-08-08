package com.arturkida.popularmovies_kotlin.ui.popular.ui.movies

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.arturkida.popularmovies_kotlin.data.ApiImpl
import com.arturkida.popularmovies_kotlin.data.ApiResponse
import com.arturkida.popularmovies_kotlin.model.Genre

class MoviesViewModel() : ViewModel() {

    val genres = MutableLiveData<List<Genre>>()

    fun getPopularMovies() {
        if (!genres.value.isNullOrEmpty()) {
            getGenres()
        }
    }

    private fun getGenres() {
        ApiImpl().getGenres(object: ApiResponse<List<Genre>> {
            override fun sucess(result: List<Genre>) {
                genres.postValue(result)
            }

            override fun failure(error: Throwable?) {
                // TODO Implement on failure
            }
        })
    }
}
