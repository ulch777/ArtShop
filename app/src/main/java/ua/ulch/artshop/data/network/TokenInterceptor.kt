package ua.ulch.artshop.data.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class TokenInterceptor  //    private final SharedPreferencesStorage preferencesStorage;
//
@Inject constructor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var original: Request = chain.request()
        val requestBuilder: Builder = original.newBuilder()
        original = requestBuilder.build()
        return chain.proceed(original)
    }
}