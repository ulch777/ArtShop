package ua.ulch.artshop.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.ulch.artshop.data.helper.LocaleHelper
import ua.ulch.artshop.data.helper.LocaleHelperImpl
import ua.ulch.artshop.data.preference.AppPreference
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleHelperEntryPoint {
    val localeHelper: LocaleHelper
}

@Module
@InstallIn(SingletonComponent::class)
object LocaleHelperModule {

    @Singleton
    @Provides
    fun provideLocaleHelper(
        @ApplicationContext context: Context?,
        appPreference: AppPreference
    ): LocaleHelper {
        return LocaleHelperImpl(appPreference, context)
    }
}