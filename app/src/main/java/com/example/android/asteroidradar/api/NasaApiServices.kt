package com.example.android.asteroidradar.api

import com.example.android.asteroidradar.database.PictureOfDay
import com.example.android.asteroidradar.utils.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class NasaApiFilter(val value: String) {
    SHOW_TODAY("rent"), SHOW_ALL("buy"), SHOW_WEEK("all")
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()).build()

private val retrofitObject = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL).build()

interface NasaApiServices {

    @GET("neo/rest/v1/feed")
    fun getItems(
        @Query("start_date") startDate: String,
        @Query("api_key") key: String
    ): Deferred<String>

    @GET("planetary/apod")
    fun getImage(@Query("api_key") key: String): Deferred<PictureOfDay>
}

object NasaApi {
    val retrofitService: NasaApiServices by lazy {
        retrofitObject.create(NasaApiServices::class.java)
    }
}
