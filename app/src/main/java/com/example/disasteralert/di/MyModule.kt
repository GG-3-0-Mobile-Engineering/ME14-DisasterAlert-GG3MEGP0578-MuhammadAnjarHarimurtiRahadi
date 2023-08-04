package com.example.disasteralert.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.work.WorkManager
import com.example.disasteralert.BuildConfig
import com.example.disasteralert.data.DisasterRepositoryImpl
import com.example.disasteralert.data.local.room.DisasterDao
import com.example.disasteralert.data.local.room.DisasterDatabase
import com.example.disasteralert.data.remote.service.DisasterAPI
import com.example.disasteralert.domain.repository.DisasterRepository
import com.example.disasteralert.helper.Constant
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): Interceptor {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Singleton
    @Provides
    fun providesFlipperOkhttpInterceptor(
        networkFlipper: NetworkFlipperPlugin,
    ): FlipperOkhttpInterceptor = FlipperOkhttpInterceptor(networkFlipper, true)

    @Provides
    @Singleton
    fun provideNetworkFlipperPlugin() : NetworkFlipperPlugin = NetworkFlipperPlugin()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: Interceptor,
        flipperInterceptor: FlipperOkhttpInterceptor
    ):OkHttpClient {
        val okHttpBuilder =  OkHttpClient.Builder()
        okHttpBuilder.apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(flipperInterceptor)
        }
        return okHttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideDisasterApi(retrofit: Retrofit) : DisasterAPI {
        return retrofit.create(DisasterAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): DisasterDatabase = Room.databaseBuilder(
        context,
        DisasterDatabase::class.java, "Disaster.db"
    ).build()

    @Provides
    @Singleton
    fun provideRoomDao(disasterDatabase: DisasterDatabase): DisasterDao = disasterDatabase.disasterDao()

    @Provides
    @Singleton
    fun provideRepository(disasterAPI: DisasterAPI, disasterDao: DisasterDao) : DisasterRepository {
        return DisasterRepositoryImpl(disasterAPI, disasterDao)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("settings") }
        )
    }
}