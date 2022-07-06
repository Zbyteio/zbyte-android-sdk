package com.zbyte.nftsdk.model

import com.zbyte.nftsdk.BuildConfig
import com.zbyte.nftsdk.IS_TEST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Object class to instantiate the Retrofit object
 *
 * @since 30/06/2022
 * @author Yash Parikh
 */
object ApiService {

    private val base_url = if (IS_TEST) BuildConfig.API_URL_TEST else BuildConfig.API_URL_PROD

    fun getInstance(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }
}