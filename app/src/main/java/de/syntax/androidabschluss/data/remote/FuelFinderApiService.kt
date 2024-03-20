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

//https://creativecommons.tankerkoenig.de/json/list.php?lat=52.45277&lng=9.75456&rad=4&type=all&apikey=eaefa201-66ec-a001-e4ee-8a91dfdcc983
const val BASE_URL = "https://creativecommons.tankerkoenig.de/"

private val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().apply{
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


interface FuelFinderApiService{
    @GET("json/list.php?lat=52.45277&lng=9.75456&rad=4&type=all&")
    suspend fun getStations(
        @Query("apikey") key: String
    ): ApiResponse

}

object FuelFinderApi{
    val retrofitService: FuelFinderApiService by lazy { retrofit.create(FuelFinderApiService::class.java) }
}