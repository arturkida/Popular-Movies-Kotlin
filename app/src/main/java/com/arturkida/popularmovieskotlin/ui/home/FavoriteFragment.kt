package com.arturkida.popularmovieskotlin.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import com.arturkida.popularmovieskotlin.R
import com.arturkida.popularmovieskotlin.adapter.MoviesListAdapter
import com.arturkida.popularmovieskotlin.data.local.MovieRepository
import com.arturkida.popularmovieskotlin.model.Genre
import com.arturkida.popularmovieskotlin.model.Movie
import com.arturkida.popularmovieskotlin.ui.details.DetailsActivity
import com.arturkida.popularmovieskotlin.utils.Constants
import com.arturkida.popularmovieskotlin.utils.SearchType
import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : Fragment(), MoviesListAdapter.MovieItemClickListener {

    private var genresList = mutableListOf<Genre>()
    private var moviesList = mutableListOf<Movie>()
    private var filteredList = mutableListOf<Movie>()

    private val viewModel by lazy {
        val repository = MovieRepository(context)
        val factory = MoviesViewModelFactory(repository)
        ViewModelProviders.of(this, factory)
            .get(MoviesViewModel::class.java)
    }
    private val adapter: MoviesListAdapter by lazy {
        MoviesListAdapter(context, moviesList, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupFragment()
    }

    fun setupFragment() {
        setRecyclerView()
        setListeners()
        removeFocus()

        getGenres()
        getFavoriteMovies()
    }

    private fun clearMoviesList() {
        filteredList.clear()
    }

    private fun setListeners() {
        setSearchListenerByMovieTitle()
        setSearchListenerByMovieYear()
    }

    private fun setSearchListenerByMovieTitle() {
        et_search_favorite_movies_title.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                clearMoviesList()
                searchMoviesBy(SearchType.TITLE, et_search_favorite_movies_title)
                hideKeyboard()
            }
            false
        }
    }

    private fun setSearchListenerByMovieYear() {
        et_search_favorite_movies_year.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                clearMoviesList()
                searchMoviesBy(SearchType.YEAR, et_search_favorite_movies_year)
                hideKeyboard()
            }
            false
        }
    }

    private fun searchMoviesBy(searchType: SearchType, searchBar: EditText) {
        val searchString = searchBar.text.toString()

        if (searchString.isBlank()) {
            adapter.updateMovies(moviesList)
            showMovieScreen(moviesList)
        } else {
            val filteredMovies = viewModel.searchMovies(
                searchString,
                searchType,
                moviesList
            )

            updateMoviesListBy(filteredMovies, searchBar)
            showMovieScreen(filteredMovies)
        }
    }

    private fun updateMoviesListBy(filteredMovies: MutableList<Movie>, searchBar: EditText) {
        Log.i(Constants.LOG_INFO, "Updating favorite movies list with search by title")
        clearMoviesList()
        filteredList.addAll(filteredMovies)
        adapter.updateMovies(filteredList)
        showMovieScreen(filteredList)
        searchBar.text.clear()
    }

    private fun removeFocus() {
        fragment_favorite_movies.requestFocus()
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.window?.currentFocus?.windowToken, 0)
    }

    private fun getGenres() {
        viewModel.getGenres().observe(this, Observer {genres ->
            genres?.let {
                genresList.clear()
                genresList.addAll(it)
            }
        })
    }

    private fun getFavoriteMovies() {
        viewModel.getFavoritesMovies()?.observe(this, Observer {favorites ->
            moviesList.clear()

            favorites?.let {
                moviesList.addAll(it)
                showMovieScreen(it)
            }

            adapter.updateMovies(moviesList)
            Log.i("favoritos", moviesList.toString())
        })
    }

    private fun showMovieScreen(moviesList: List<Movie>) {
        hideProgressBar()

        if (viewModel.mustShowMoviesList(moviesList)) {
            showFavoriteMoviesList()
        } else {
            showEmptyListMessage()
        }
    }

    private fun showEmptyListMessage() {
        rv_favorite_movie_list.visibility = View.GONE
        tv_empty_favorites_list.visibility = View.VISIBLE
    }

    private fun showFavoriteMoviesList() {
        rv_favorite_movie_list.visibility = View.VISIBLE
        tv_empty_favorites_list.visibility = View.GONE
    }

    private fun hideProgressBar() {
        progress_bar_favorites.visibility = View.GONE
    }

    private fun setRecyclerView() {
        rv_favorite_movie_list.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        context?.let {
            rv_favorite_movie_list.adapter = adapter
        }
    }

    override fun updateFavorite(position: Int) {
        if (moviesList[position].favorite) {
            viewModel.addFavoriteMovie(moviesList[position])
        } else {
            viewModel.deleteFavoriteMovie(moviesList[position])
        }
    }

    override fun onClick(movie: Movie) {
        val intent = DetailsActivity.getIntent(context)

        intent.putExtra(Constants.INTENT_MOVIE_INFO, movie)

        startActivity(intent)

        Log.i(Constants.LOG_INFO, "Starting Details Activity from favorite movies list")
        Log.i(Constants.LOG_INFO, "Movie data: $movie")
    }
}
