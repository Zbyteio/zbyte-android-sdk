package com.zbyte.nftsdk.model

import android.util.Log
import com.zbyte.nftsdk.BuildConfig
import com.zbyte.nftsdk.COOKIE_REFRESH_TOKEN
import com.zbyte.nftsdk.COOKIE_TOKEN
import com.zbyte.nftsdk.CUSTOM_API_URL
import okhttp3.OkHttpClient
import okhttp3.Request
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

    //private val base_url = if (IS_TEST) BuildConfig.API_URL_TEST else BuildConfig.API_URL_PROD
    private val base_url = BuildConfig.API_URL_DEV

    private fun baseUrl(): String {
        return CUSTOM_API_URL.ifEmpty { base_url }
    }

    fun getInstance(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        Log.e("COOKIE TOKEN", COOKIE_TOKEN)
        Log.e("COOKIE REFRESH TOKEN", COOKIE_REFRESH_TOKEN)

        httpClient.addInterceptor { chain ->
            val original: Request = chain.request()
            val authorized: Request = original.newBuilder()
                .addHeader("Cookie", "accessToken=$COOKIE_TOKEN")
                .addHeader("Cookie", "refreshToken=$COOKIE_REFRESH_TOKEN")
                .build()
            chain.proceed(authorized)
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }
}