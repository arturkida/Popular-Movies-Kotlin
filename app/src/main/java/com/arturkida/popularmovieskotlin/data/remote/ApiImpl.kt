package com.arturkida.popularmovieskotlin.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.arturkida.popularmovieskotlin.BuildConfig
import com.arturkida.popularmovieskotlin.idlingresource.EspressoIdlingResource
import com.arturkida.popularmovieskotlin.model.Genre
import com.arturkida.popularmovieskotlin.model.Movie
import com.arturkida.popularmovieskotlin.model.ResultGenres
import com.arturkida.popularmovieskotlin.model.ResultMovies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiImpl {

    private val genres = MutableLiveData<List<Genre>?>()
    private val movies = MutableLiveData<List<Movie>?>()

    companion object {
        val retrofit: Api = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    fun getGenres(): LiveData<List<Genre>?> {
        val call = retrofit.getGenres(BuildConfig.MOVIEDB_API_KEY)

        EspressoIdlingResource.increment()

        call.enqueue(object : Callback<ResultGenres?> {
            override fun onFailure(call: Call<ResultGenres?>?, t: Throwable?) {
                genres.value = null
                EspressoIdlingResource.decrement()
            }
            override fun onResponse(call: Call<ResultGenres?>?, response: Response<ResultGenres?>?) {
                response?.body()?.let {
                    genres.value = it.genres
                    EspressoIdlingResource.decrement()
                }
            }
        })

        return genres
    }

    fun getPopularMovies() : LiveData<List<Movie>?> {
        val call = retrofit.getPopularMovies(BuildConfig.MOVIEDB_API_KEY)

        EspressoIdlingResource.increment()

        call.enqueue(object : Callback<ResultMovies?> {
            override fun onFailure(call: Call<ResultMovies?>?, t: Throwable?) {
                movies.value = null
                EspressoIdlingResource.decrement()
            }
            override fun onResponse(call: Call<ResultMovies?>?, response: Response<ResultMovies?>?) {
                response?.body()?.let {
                    movies.value = it.results
                    EspressoIdlingResource.decrement()
                }
            }
        })

        return movies
    }
}