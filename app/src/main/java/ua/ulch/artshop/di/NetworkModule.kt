package ua.ulch.artshop.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import ua.ulch.artshop.BuildConfig
import ua.ulch.artshop.data.Constants.RU_LANG_ID
import ua.ulch.artshop.data.Constants.UK_LANG_ID
import ua.ulch.artshop.data.helper.LocaleHelper
import ua.ulch.artshop.data.network.ArtShopApi
import ua.ulch.artshop.data.network.HostSelectionInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CONTENT_HEADER_KEY = "Content-Type"
    private const val CONTENT_HEADER_VALUE = "application/json"

    private const val OK_HTTP_TAG = "OkHttp"

    private val LOGGING_LEVEL = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.tag(OK_HTTP_TAG).d(message)
        }.apply {
            level = LOGGING_LEVEL
        }
    }

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader(CONTENT_HEADER_KEY, CONTENT_HEADER_VALUE)
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideHostSelectionInterceptor(localeHelper: LocaleHelper): HostSelectionInterceptor {
        return HostSelectionInterceptor(localeHelper)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor,
        hostSelectionInterceptor: HostSelectionInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(interceptor)
            .addInterceptor(hostSelectionInterceptor)
            .connectTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
            .readTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder
            .setLenient()
            .setPrettyPrinting()
            .create()
    }

    @Singleton
    @Provides
    fun provideArtShopApi(
        gson: Gson,
        okHttpClient: OkHttpClient,
        localeHelper: LocaleHelper
    ): ArtShopApi {
        return createApi(
            ArtShopApi::class.java,
            okHttpClient,
            GsonConverterFactory.create(gson),
            localeHelper
        )
    }

    private fun <T> createApi(
        serviceApi: Class<T>,
        okHttpClient: OkHttpClient,
        factoryConverter: Converter.Factory,
        localeHelper: LocaleHelper
    ): T {
        val builder = Retrofit.Builder()
        builder.client(okHttpClient)
            .baseUrl(
                when (localeHelper.getCurrentLocale()) {
                    UK_LANG_ID,
                    RU_LANG_ID -> BuildConfig.BASE_URL_UK

                    else -> BuildConfig.BASE_URL_EN
                }
            )
            .addConverterFactory(factoryConverter)
        return builder.build().create(serviceApi)

    }
}