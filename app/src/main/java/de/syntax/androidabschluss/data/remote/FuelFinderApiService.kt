package de.syntax.androidabschluss.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.syntax.androidabschluss.data.model.ApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://creativecommons.tankerkoenig.de/"

private val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()


interface FuelFinderApiService {

    //get Station with device Location
    @GET("json/list.php?type=all&")
    suspend fun getStationsWithLocation(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("rad") radius: Int,
        @Query("apikey") key: String
    ): ApiResponse

}

object FuelFinderApi {
    val retrofitService: FuelFinderApiService by lazy { retrofit.create(FuelFinderApiService::class.java) }
}