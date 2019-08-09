package com.arturkida.popularmovies_kotlin.ui.home

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
import com.arturkida.popularmovies_kotlin.utils.SearchType
import kotlinx.android.synthetic.main.fragment_movies.*

class PopularFragment : BaseFragment() {

    private var genresList = mutableListOf<Genre>()
    private var moviesList = mutableListOf<Movie>()

    private lateinit var viewModel: MoviesViewModel
    private val adapter: MoviesListAdapter by lazy {
            MoviesListAdapter(context, moviesList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movies, container, false)
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
        et_search_popular_movies.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val filteredMovies = viewModel.searchMovies(et_search_popular_movies.text.toString(), SearchType.TITLE, viewModel.popularMovies)

                Log.i(Constants.LOG_INFO, "Updating popular movies list with search by title")
                adapter.updateMovies(filteredMovies)

                et_search_popular_movies.text.clear()
            }
            false
        }
    }

    private fun removeFocus() {
        fragment_popular_movies.requestFocus()
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
                Log.i(Constants.LOG_INFO, "Popular movies updated")
            }
        })
    }

    private fun setRecyclerView() {
        rv_popular_movie_list.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        context?.let {
            rv_popular_movie_list.adapter = adapter
        }
    }

    private fun getPopularMovies() {
        viewModel.getPopularMovies()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(activity!!)
            .get(MoviesViewModel::class.java)
    }
}