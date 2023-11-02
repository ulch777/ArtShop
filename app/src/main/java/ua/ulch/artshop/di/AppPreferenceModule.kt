package ua.ulch.artshop.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.ulch.artshop.data.preference.AppPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppPreferenceModule {

    @Singleton
    @Provides
    fun provideAppPreference(@ApplicationContext context: Context?): AppPreference {
        return AppPreference(context)
    }
}