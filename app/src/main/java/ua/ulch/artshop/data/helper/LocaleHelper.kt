package ua.ulch.artshop.data.helper

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface LocaleHelper {
    fun onAttach(): Context?
    fun getCurrentLocale(): String
    suspend fun observeCurrentLocale(): Flow<String>
    fun setCurrentLocale(language: String): Context?
}