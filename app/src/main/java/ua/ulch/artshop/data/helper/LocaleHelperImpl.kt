package ua.ulch.artshop.data.helper

import android.content.Context
import android.os.Build
import android.os.LocaleList
import kotlinx.coroutines.flow.Flow
import ua.ulch.artshop.data.Constants.EN_LANG_ID
import ua.ulch.artshop.data.Constants.LANG_ID
import ua.ulch.artshop.data.Constants.RU_LANG_ID
import ua.ulch.artshop.data.Constants.UK_LANG_ID
import ua.ulch.artshop.data.preference.AppPreference
import java.util.Locale
import javax.inject.Inject

class LocaleHelperImpl @Inject constructor(
    private val appPreference: AppPreference,
    private val context: Context?
) : LocaleHelper {

    override fun onAttach(): Context? {
        val lang = getCurrentLocale()
        return if (lang.isEmpty()) {
            when (getSystemLocale()) {
                UK_LANG_ID,
                RU_LANG_ID -> setCurrentLocale(UK_LANG_ID)

                else -> setCurrentLocale(EN_LANG_ID)
            }
        } else setCurrentLocale(lang)
    }

    override fun getCurrentLocale(): String {
        return appPreference.loadDataString(LANG_ID)
    }

    override suspend fun observeCurrentLocale(): Flow<String> {
        return appPreference.loadDataStringFlow(LANG_ID)
    }

    override fun setCurrentLocale(language: String): Context? {
        appPreference.saveDataString(LANG_ID, language)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language)
        }
        val locale = Locale(language)
        return context?.apply {
            baseSetLocale(this, locale)
        }
    }

    private fun getSystemLocale(): String {
        return Locale.getDefault().language
    }

    private fun updateResources(context: Context?, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context?.resources?.configuration
        configuration?.setLocale(locale)
        return configuration?.let {
            context.createConfigurationContext(it)
        }
    }

    private fun baseSetLocale(context: Context, newLocale: Locale): Context {
        var tmpContext = context
        val res = tmpContext.resources
        val configuration = res.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale)
            val localeList = LocaleList(newLocale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            tmpContext = tmpContext.createConfigurationContext(configuration)
        } else
            configuration.setLocale(newLocale)
        tmpContext.createConfigurationContext(configuration)
        return tmpContext
    }
}