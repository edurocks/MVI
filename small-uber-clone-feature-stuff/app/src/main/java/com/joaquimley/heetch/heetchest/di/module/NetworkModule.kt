package com.joaquimley.heetch.heetchest.di.module

import android.app.Application
import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.joaquimley.heetch.heetchest.BuildConfig
import com.joaquimley.heetch.heetchest.data.remote.HiringHeetchApi
import com.joaquimley.heetch.heetchest.di.qualifier.ApiImageUrl
import com.joaquimley.heetch.heetchest.di.qualifier.ApiUrl
import com.joaquimley.heetch.heetchest.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context) = Cache(context.cacheDir, 10 * 1024 * 1024.toLong())

    @Provides
    @Singleton
    internal fun provideRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Log.d("Retrofit logging", message)
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideLoggingCapableHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
        gson: Gson,
        rxJavaCallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit.Builder {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxJavaCallAdapterFactory)
            .client(okHttpClient)
    }

    @ApiUrl
    @Provides
    fun provideApiBaseUrl(): String {
        return "http://hiring.heetch.com/mobile/"
    }

    @ApiImageUrl
    @Provides
    fun provideApiImageBaseUrl(): String {
        return "http://hiring.heetch.com"
    }

    @Provides
    @Singleton
    fun provideRestService(retrofitBuilder: Retrofit.Builder, @ApiUrl baseUrl: String): HiringHeetchApi {
        return retrofitBuilder.baseUrl(baseUrl)
            .build()
            .create(HiringHeetchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGsonConverter(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun providesRequestManager(application: Application): RequestManager {
        // Disk and memory cache can be changed here. Follow descriptions in glide documentation.a
        // See: https://bumptech.github.io/glide/doc/download-setup.html
        return Glide.with(application).setDefaultRequestOptions(
            (RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        )
    }


}
