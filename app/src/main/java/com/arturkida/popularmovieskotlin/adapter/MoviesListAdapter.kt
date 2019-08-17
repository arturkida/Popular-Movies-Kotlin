package com.arturkida.popularmovieskotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arturkida.popularmovieskotlin.BuildConfig
import com.arturkida.popularmovieskotlin.R
import com.arturkida.popularmovieskotlin.model.Movie
import com.arturkida.popularmovieskotlin.utils.Constants
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie_info.view.*

class MoviesListAdapter(private val context: Context?,
                        private var movies: MutableList<Movie>,
                        private val listener: MovieItemClickListener)
    : RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie_info, parent, false)

        return MoviesListViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) = holder.bind(movies[position])

    fun updateMovies(movies: MutableList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    inner class MoviesListViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.container_item_list.setOnClickListener {
                listener.onClick(movies[adapterPosition])
            }

            itemView.iv_item_favorite_star.setOnClickListener {
                updateFavorites()
                listener.updateFavorite(adapterPosition)
            }
        }

        fun bind(movie: Movie) {
            setMovieTitle(movie)
            setMovieDate(movie)
            setMoviePoster(movie)
            setFavorite()
        }

        private fun setMovieDate(movie: Movie) {
            itemView.tv_movie_year.text = movie.release_date
        }

        private fun setMovieTitle(movie: Movie) {
            itemView.tv_movie_title.text = movie.title
        }

        private fun setMoviePoster(movie: Movie) {
            val posterPath = BuildConfig.BASE_IMAGE_URL + movie.poster_path

            Glide.with(context)
                .load(posterPath)
                .into(itemView.iv_movie_poster)
        }

        private fun setFavorite() {
            if (movies[adapterPosition].favorite) {
                itemView.iv_item_favorite_star.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                itemView.iv_item_favorite_star.setImageResource(android.R.drawable.btn_star_big_off)
            }
        }

        private fun updateFavorites() {
            if (movies[adapterPosition].favorite) {
                itemView.iv_item_favorite_star.setImageResource(android.R.drawable.btn_star_big_off)
                movies[adapterPosition].favorite = false
            } else {
                itemView.iv_item_favorite_star.setImageResource(android.R.drawable.btn_star_big_on)
                movies[adapterPosition].favorite = true
            }
        }

        override fun onClick(v: View?) {
            listener.onClick(movies[adapterPosition])
        }
    }

    interface MovieItemClickListener {
        fun onClick(movie: Movie)
        fun updateFavorite(position: Int)
    }
}