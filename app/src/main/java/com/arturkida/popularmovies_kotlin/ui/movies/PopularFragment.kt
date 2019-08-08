package com.arturkida.popularmovies_kotlin.ui.movies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.arturkida.popularmovies_kotlin.R
import com.arturkida.popularmovies_kotlin.adapter.MoviesListAdapter
import com.arturkida.popularmovies_kotlin.model.Genre
import com.arturkida.popularmovies_kotlin.model.Movie
import com.arturkida.popularmovies_kotlin.ui.BaseFragment
import com.arturkida.popularmovies_kotlin.utils.Constants
import kotlinx.android.synthetic.main.movies_fragment.*

class PopularFragment : BaseFragment() {

    companion object {
        fun newInstance() = PopularFragment()
    }

    private lateinit var viewModel: MoviesViewModel
    private val adapter: MoviesListAdapter by lazy {
            MoviesListAdapter(context, moviesList)
    }

    private var genresList = mutableListOf<Genre>()
    private var moviesList = mutableListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.movies_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupFragment()
    }

    fun setupFragment() {
        setViewModel()
        setRecyclerView()
        setObservers()
        setListeners()




        removeFocus()

        getPopularMovies()
    }

    private fun setListeners() {
        et_search_movies.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val filteredMovies = viewModel.searchMovies(et_search_movies.text.toString())

                Log.i(Constants.LOG_INFO, "Updating movie list with search criteria")
                adapter.updateMovies(filteredMovies)
            }
            false
        }
    }

    private fun removeFocus() {
        movies.requestFocus()
    }

    private fun setObservers() {
        viewModel.genres.observe(this, Observer { genres ->
            genres?.let {
                genresList.addAll(it)
                Log.i(Constants.LOG_INFO, "Genres updated")
            }
        })

        viewModel.popularMovies.observe(this, Observer { movies ->
            movies?.let {
                moviesList.addAll(it)
                adapter.updateMovies(moviesList)
                Log.i(Constants.LOG_INFO, "Movies updated")
            }
        })
    }

    private fun setRecyclerView() {
        rv_movie_list.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        context?.let {
            rv_movie_list.adapter = adapter
        }
    }

    private fun getPopularMovies() {
        viewModel.getPopularMovies()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this)
            .get(MoviesViewModel::class.java)
    }
}