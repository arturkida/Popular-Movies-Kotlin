package com.arturkida.popularmovieskotlin.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.arturkida.popularmovieskotlin.data.local.MovieRepository
import com.arturkida.popularmovieskotlin.data.remote.ApiImpl
import com.arturkida.popularmovieskotlin.model.Movie
import com.arturkida.popularmovieskotlin.utils.SearchType

class MoviesViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val genres = getGenres()
    private var popularMovies: LiveData<List<Movie>?> = getPopularMovies()
    private var favoriteMovies: LiveData<List<Movie>?> = getFavoritesMovies()
    private var filteredMovies = mutableListOf<Movie>()

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
        favoriteMovies.value?.let { favoritesList ->
            favoritesList.forEach { favoriteMovie ->
                if (favoriteMovie.id == movie.id) {
                    return true
                }
            }
        }

        return false
    }

    fun getGenres() = ApiImpl().getGenres()

    fun getPopularMovies() = ApiImpl().getPopularMovies()

    fun getFavoritesMovies() : LiveData<List<Movie>?> {
        val favoritesList = repository.getMoviesFromDatabase()

        favoritesList?.let {
            favoriteMovies = it
        }

        return favoriteMovies
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