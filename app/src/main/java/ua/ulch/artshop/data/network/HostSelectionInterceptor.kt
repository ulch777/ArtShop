package ua.ulch.artshop.data.network

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ua.ulch.artshop.BuildConfig
import ua.ulch.artshop.data.Constants.EN_LANG_ID
import ua.ulch.artshop.data.Constants.RU_LANG_ID
import ua.ulch.artshop.data.Constants.UK_LANG_ID
import ua.ulch.artshop.data.helper.LocaleHelper
import java.io.IOException
import java.net.URISyntaxException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HostSelectionInterceptor @Inject constructor(
    private val localeHelper: LocaleHelper,
) :
    Interceptor {

    private fun getHostBaseUrl(): HttpUrl? {
        return when (localeHelper.getCurrentLocale()) {
            EN_LANG_ID -> BuildConfig.BASE_URL_EN.toHttpUrlOrNull()
            UK_LANG_ID,
            RU_LANG_ID -> BuildConfig.BASE_URL_UK.toHttpUrlOrNull()

            else -> BuildConfig.BASE_URL_EN.toHttpUrlOrNull()
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        getHostBaseUrl()?.let {
            try {
                val newUrl = request.url.newBuilder()
                    .scheme(it.scheme)
                    .host(it.toUrl().toURI().host)
                    .build()

                newUrl.let { url ->
                    request = request.newBuilder()
                        .url(url)
                        .build()
                }
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
        return chain.proceed(request)
    }
}