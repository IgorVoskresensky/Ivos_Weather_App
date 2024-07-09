package com.ivos.data.network.retrofitApi

import com.ivos.common.BASE_URL
import com.ivos.data.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request()
            val newUrl = request
                .url
                .newBuilder()
                .addQueryParameter("key", BuildConfig.API_KEY)
                .build()
            val modifiedRequest = request.newBuilder()
                .url(newUrl)
                .build()

            it.proceed(modifiedRequest)
        }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp)
        .build()

    val retrofitApiService: RetrofitApiService = retrofit.create()
}
