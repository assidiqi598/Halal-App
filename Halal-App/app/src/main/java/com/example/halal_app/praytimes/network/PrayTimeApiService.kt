package com.example.halal_app.praytimes.network

import com.example.halal_app.productcheck.network.Product
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://blog.iwkz.de/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface ProductApiService{
    @GET("jdwlshalat_ibmus/")
    fun getProductAsync(): Deferred<PrayTime> //https://stackoverflow.com/a/54338882
}

object PrayTimesApi {
    val retrofitService : ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java) }
}
