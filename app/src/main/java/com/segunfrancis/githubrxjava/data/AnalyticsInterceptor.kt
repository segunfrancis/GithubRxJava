package com.segunfrancis.githubrxjava.data

import android.os.Build
import com.segunfrancis.githubrxjava.util.AppConstants.DEVICE_MODEL
import com.segunfrancis.githubrxjava.util.AppConstants.DEVICE_PLATFORM
import com.segunfrancis.githubrxjava.util.AppConstants.OS_VERSION
import okhttp3.Interceptor
import okhttp3.Response

class AnalyticsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        requestBuilder.addHeader(OS_VERSION, Build.VERSION.SDK_INT.toString())
        requestBuilder.addHeader(DEVICE_MODEL, Build.MODEL)
        requestBuilder.addHeader(DEVICE_PLATFORM, "android")

        return chain.proceed(requestBuilder.build())
    }
}