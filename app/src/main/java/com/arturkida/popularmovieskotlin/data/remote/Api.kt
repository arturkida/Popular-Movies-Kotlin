package com.arturkida.popularmovieskotlin.data.remote

import com.arturkida.popularmovieskotlin.BuildConfig
import com.arturkida.popularmovieskotlin.model.ResultGenres
import com.arturkida.popularmovieskotlin.model.ResultMovies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET(BuildConfig.GENRES)
    fun getGenres(@Query("api_key") api_key: String) : Call<ResultGenres>

    @GET(BuildConfig.POPULAR_MOVIES)
    fun getPopularMovies(@Query("api_key") api_key: String) : Call<ResultMovies>
}