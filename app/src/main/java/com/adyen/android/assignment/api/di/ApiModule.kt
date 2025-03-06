package com.adyen.android.assignment.api.di

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.repoimpl.PlacesRepositoryImpl
import com.adyen.android.assignment.places.repo.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(20L, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val header = chain.request()
                    .newBuilder()
                    .header("Authorization", BuildConfig.API_KEY)
                    .build()
                chain.proceed(header)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providesPlacesService(retrofit: Retrofit): PlacesService =
        retrofit.create(PlacesService::class.java)

    @Provides
    @Singleton
    fun providesPlacesRepository(service: PlacesService): PlacesRepository =
        PlacesRepositoryImpl(service)
}
