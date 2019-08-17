package com.arturkida.popularmovieskotlin.ui.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.arturkida.popularmovieskotlin.data.local.MovieRepository
import com.arturkida.popularmovieskotlin.data.remote.ApiImpl
import com.arturkida.popularmovieskotlin.data.remote.ApiResponse
import com.arturkida.popularmovieskotlin.idlingresource.EspressoIdlingResource
import com.arturkida.popularmovieskotlin.model.Genre
import com.arturkida.popularmovieskotlin.model.Movie
import com.arturkida.popularmovieskotlin.utils.SearchType

class MoviesViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val genres = getGenres()
    val popularMovies: MutableLiveData<List<Movie>>? = MutableLiveData()
    var filteredMovies = mutableListOf<Movie>()

    val favoriteMovies = repository.allFavoriteMovies

    private var retryGetGenresCount = 0

    fun retryGetGenres() {
        retryGetGenresCount++

        if (retryGetGenresCount <= 3) {
            getGenres()
        }
    }

    fun mustShowMoviesList(moviesList: List<Movie>) = moviesList.isNotEmpty()

    fun populateGenresNameFrom(movie: Movie): Movie {

        movie.genre_names = ""
        genres.value?.let {genresList ->
            genresList.forEach {genre ->
                if (movie.genre_ids.contains(genre.id)) {
                    if (movie.genre_names == "") {
                        movie.genre_names = genre.name
                    } else {
                        movie.genre_names += ", ${genre.name}"
                    }
                }
            }
        }

        return movie
    }

    fun updateFavoriteStatusOf(movie: Movie): Movie {
        movie.favorite = isMovieFavorited(movie)

        return movie
    }

    private fun isMovieFavorited(movie: Movie): Boolean {
        favoriteMovies?.value?.let { favoritesList ->
            favoritesList.forEach { favoriteMovie ->
                if (favoriteMovie.id == movie.id) {
                    return true
                }
            }
        }

        return false
    }

    fun getPopularMovies() {
        if (genres.value.isNullOrEmpty()) {
            getGenres()
        }

        EspressoIdlingResource.increment()
        ApiImpl()
            .getPopularMovies(object: ApiResponse<List<Movie>> {
            override fun onSuccess(result: List<Movie>) {
                EspressoIdlingResource.decrement()
                postPopularMoviesResult(result)
            }

            override fun onFailure(error: Throwable?) {
                EspressoIdlingResource.decrement()
                postPopularMoviesResult(listOf())
            }
        })
    }

    fun postPopularMoviesResult(result: List<Movie>) {
        popularMovies?.postValue(result)
    }

    fun getGenres() : LiveData<List<Genre>?> {
        EspressoIdlingResource.increment()
        return ApiImpl().getGenres()
    }

    fun searchMovies(searchText: String, searchType: SearchType, searchList: List<Movie>): MutableList<Movie> {

        when(searchType) {
            SearchType.TITLE ->
                if (searchList.isNotEmpty()) {
                    filteredMovies = searchList.filter { movie ->
                        movie.title.toLowerCase().contains(searchText.toLowerCase())
                    } as MutableList<Movie>
                }
            SearchType.YEAR ->
                if (searchList.isNotEmpty()) {
                    filteredMovies = searchList.filter { movie ->
                        movie.release_date.contains(searchText)
                    } as MutableList<Movie>
                }
        }

        return filteredMovies
    }

    fun addFavoriteMovie(movie: Movie) = repository.addMovie(movie)

    fun deleteFavoriteMovie(movie: Movie) = repository.deleteMovie(movie)
}
