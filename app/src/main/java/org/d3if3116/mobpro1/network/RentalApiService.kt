package org.d3if3116.mobpro1.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3116.mobpro1.model.Rental
import org.d3if3116.mobpro1.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://ariqathllh.my.id/other/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RentalApiService {
    @GET("json.php")
    suspend fun getRental(
        @Query("auth") userId: String
    ): List<Rental>

    @Multipart
    @POST("json.php")
    suspend fun postRental(
        @Part("auth") userId: String,
        @Part("name") name: RequestBody,
        @Part("vehicle") vehicle: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("json.php")
    suspend fun deleteRental(
        @Query("auth") userId: String,
        @Query("id") id: String
    ): OpStatus
}

object RentalApi {
    val service: RentalApiService by lazy {
        retrofit.create(RentalApiService::class.java)
    }
    fun getRentalUrl(gambar: String): String {
        return "${BASE_URL}$gambar"
    }
}
enum class ApiStatus { LOADING, SUCCESS, FAILED}