package com.arturkida.popularmovieskotlin.data.remote

import android.util.Log
import com.arturkida.popularmovieskotlin.BuildConfig
import com.arturkida.popularmovieskotlin.model.Genre
import com.arturkida.popularmovieskotlin.model.Movie
import com.arturkida.popularmovieskotlin.model.ResultGenres
import com.arturkida.popularmovieskotlin.model.ResultMovies
import com.arturkida.popularmovieskotlin.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiImpl {

    companion object {
        val retrofit: Api = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    fun getGenres(callback: ApiResponse<List<Genre>>) {
        val call = retrofit.getGenres(BuildConfig.MOVIEDB_API_KEY)

        call.enqueue(object : Callback<ResultGenres?> {
            override fun onFailure(call: Call<ResultGenres?>?, t: Throwable?) {
                Log.e(Constants.LOG_INFO, t?.message)
                callback.onFailure(t)
            }
            override fun onResponse(call: Call<ResultGenres?>?, response: Response<ResultGenres?>?) {
                response?.body()?.let {
                    callback.onSuccess(it.genres)
                }
            }
        })
    }

    fun getPopularMovies(callback: ApiResponse<List<Movie>>) {
        val call = retrofit.getPopularMovies(BuildConfig.MOVIEDB_API_KEY)

        call.enqueue(object : Callback<ResultMovies?> {
            override fun onFailure(call: Call<ResultMovies?>?, t: Throwable?) {
                Log.e(Constants.LOG_INFO, t?.message)
                callback.onFailure(t)
            }
            override fun onResponse(call: Call<ResultMovies?>?, response: Response<ResultMovies?>?) {
                response?.body()?.let {
                    callback.onSuccess(it.results)
                }
            }
        })
    }
}