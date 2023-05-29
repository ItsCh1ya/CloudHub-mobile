package com.example.cloudhub.common

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class CookieInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val modifiedRequest = addCookieToRequest(request)
        val response = chain.proceed(modifiedRequest)
        saveCookieFromResponse(response)
        return response
    }

    private fun addCookieToRequest(request: Request): Request {
        val cookie = getCookieFromSharedPreferences()
        return if (!cookie.isNullOrEmpty()) {
            val modifiedHeaders = request.headers.newBuilder()
                .add("Cookie", cookie)
                .build()
            request.newBuilder()
                .headers(modifiedHeaders)
                .build()
        } else {
            request
        }
    }

    private fun saveCookieFromResponse(response: Response) {
        val headers = response.headers("Set-Cookie")
        if (headers.isNotEmpty()) {
            val cookie = headers[0] // Assuming there is only one cookie in the header
            saveCookieToSharedPreferences(cookie)
        }
    }

    private fun getCookieFromSharedPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)
        return sharedPreferences.getString("cookie", null)
    }

    private fun saveCookieToSharedPreferences(cookie: String) {
        val sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("cookie", cookie)
        editor.apply()
    }
}

fun isCookieExist(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)
    return sharedPreferences.getString("cookie", null) != null
}

fun delCookie(context: Context) {
    val sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.remove("cookies")
    editor.apply()
}