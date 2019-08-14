package com.arturkida.popularmovieskotlin.data.remote

interface ApiResponse<T> {
    fun onSuccess(result: T)
    fun onFailure(error: Throwable?)
}