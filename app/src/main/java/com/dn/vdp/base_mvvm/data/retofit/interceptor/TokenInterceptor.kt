package com.dn.vdp.base_mvvm.data.retofit.interceptor

import com.dn.vdp.base_mvvm.data.local.prefrence.AppPrefKey
import com.dn.vdp.base_mvvm.data.local.prefrence.AppSharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

private const val contentType = "Content-Type"
private const val contentTypeValue = "application/json"
private const val authorization = "Authorization"

@Singleton
class TokenInterceptor @Inject constructor(
    private val preferences: AppSharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = initializeRequestWithHeaders(chain.request())
        val response = chain.proceed(request)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            preferences.clearKey(AppPrefKey.ACCESS_TOKEN)
            preferences.clearKey(AppPrefKey.TOKEN_REGISTER)
            preferences.clearKey(AppPrefKey.CURRENT_USER)
        }
        val responseBody = response.body
        val responseBodyString = response.body?.string()
        return createNewResponse(response, responseBody, responseBodyString)
    }

    private fun initializeRequestWithHeaders(request: Request): Request {
        val token = preferences.get(AppPrefKey.TOKEN_REGISTER, String::class.java)
        return request.newBuilder().apply {
            header(contentType, contentTypeValue)
            token?.let { addHeader(authorization, it) }
            method(request.method, request.body)
        }.build()
    }

    private fun createNewResponse(
        response: Response,
        responseBody: ResponseBody?,
        responseBodyString: String?,
    ): Response {
        val contentType = responseBody?.contentType()
        return response.newBuilder()
            .body((responseBodyString ?: "").toResponseBody(contentType))
            .build()
    }
}