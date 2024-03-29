package com.arturkida.popularmovieskotlin.ui.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arturkida.popularmovieskotlin.BuildConfig
import com.arturkida.popularmovieskotlin.R
import com.arturkida.popularmovieskotlin.model.Movie
import com.arturkida.popularmovieskotlin.ui.MoviesViewModel
import com.arturkida.popularmovieskotlin.utils.Constants
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private val viewModel by viewModel<MoviesViewModel>()

    private lateinit var movie: Movie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupFragment()
    }

    private fun setupFragment() {
        getIntents()
        loadMovieInfo()
        setListeners()
    }

    private fun setListeners() {
        iv_favorite_star.setOnClickListener {
            if (!movie.favorite) {
                viewModel.addFavoriteMovie(movie)
                movie.favorite = true
            } else {
                viewModel.deleteFavoriteMovie(movie)
                movie.favorite = false
            }

            updateFavoriteIcon()
        }
    }

    private fun getIntents() {
        activity?.intent?.let {
            movie = it.getParcelableExtra(Constants.INTENT_MOVIE_INFO)
        }
    }

    private fun loadMovieInfo() {
        val posterPath = BuildConfig.BASE_IMAGE_URL + movie.poster_path

        Glide.with(this)
            .load(posterPath)
            .into(iv_details_movie_poster)

        tv_details_movie_title.text = movie.title
        tv_details_movie_description.text = movie.overview
        tv_details_movie_year.text = movie.release_date
        tv_details_movie_rate.text = movie.vote_average.toString()
        tv_details_movie_genres.text = movie.genre_names

        updateFavoriteIcon()
    }

    private fun updateFavoriteIcon() {
        if (movie.favorite) {
            iv_favorite_star.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            iv_favorite_star.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }
}
