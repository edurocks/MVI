package com.codingwithmitch.openapi.ui.auth.di.auth

import android.content.SharedPreferences
import com.codingwithmitch.openapi.ui.auth.api.auth.OpenApiAuthService
import com.codingwithmitch.openapi.ui.auth.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.ui.auth.persistence.AuthTokenDao
import com.codingwithmitch.openapi.ui.auth.repository.auth.AuthRepository
import com.codingwithmitch.openapi.ui.auth.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule{

    @AuthScope
    @Provides
    fun provideFakeApiService(retrofitBuilder: Retrofit.Builder): OpenApiAuthService {
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService,
        sharedPreferences: SharedPreferences,
        sharedPreferencesEditor: SharedPreferences.Editor
    ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager,
            sharedPreferences,
            sharedPreferencesEditor
        )
    }
}