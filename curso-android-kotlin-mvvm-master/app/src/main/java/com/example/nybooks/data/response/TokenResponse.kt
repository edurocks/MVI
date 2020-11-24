package com.example.nybooks.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "access_token")
    val accessToken: String?,
    @Json(name = "token_type")
    val tokenType: String?,
    @Json(name = "permissions")
    val permissions: List<String>?,
    @Json(name = "roles")
    val roles: List<String?>)
