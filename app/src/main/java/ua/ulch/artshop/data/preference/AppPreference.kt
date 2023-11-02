package ua.ulch.artshop.data.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

private const val NAME_PREF_STORAGE = "pax_pref_uploader"

@Singleton
class AppPreference @SuppressLint("CommitPrefEdits") @Inject constructor(context: Context?) {
    private val pref: SharedPreferences?
    private val editor: SharedPreferences.Editor?

    init {
        pref = context!!.getSharedPreferences(NAME_PREF_STORAGE, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun saveDataString(key: String?, value: String?) {
        if (editor != null) {
            editor.putString(key, value)
            editor.commit()
        }
    }

    fun saveDataBoolean(key: String?, value: Boolean) {
        if (editor != null) {
            editor.putBoolean(key, value)
            editor.commit()
        }
    }

    fun saveDataLong(key: String?, value: Long) {
        if (editor != null) {
            editor.putLong(key, value)
            editor.commit()
        }
    }

    fun loadDataBoolean(key: String?): Boolean {
        return pref!!.getBoolean(key, false)
    }

    fun loadDataString(key: String): String {
        return pref!!.getString(key, "") ?: ""
    }

    fun loadDataLong(key: String?): Long {
        return pref?.getLong(key, 0) ?: -1
    }

    fun loadDataStringFlow(key: String): Flow<String> {
        return pref!!.getStringFlow(key)
            .filterNotNull()
    }

}

fun SharedPreferences.getStringFlow(keyForString: String) = callbackFlow<String?> {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (keyForString == key) {
            trySend(getString(key, ""))
        }
    }
    registerOnSharedPreferenceChangeListener(listener)
    if (contains(keyForString)) {
        send(getString(keyForString, "")) // if you want to emit an initial pre-existing value
    }
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}.buffer(Channel.UNLIMITED) // so trySend never fails