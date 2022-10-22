package com.pisakov.skillproj.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pisakov.skillproj.domain.Interactor

class PreferenceProvider(context: Context) {
    private val appContext = context.applicationContext
    private val preference: SharedPreferences = appContext.getSharedPreferences(SHARED_FILE_NAME, Context.MODE_PRIVATE)

    init {
        if(preference.getBoolean(KEY_FIRST_LAUNCH, false)) {
            preference.edit { putString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) }
            preference.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
        }
    }

    fun saveDefaultCategory(category: String) {
        preference.edit { putString(KEY_DEFAULT_CATEGORY, category) }
    }

    fun getDefaultCategory(): String {
        return preference.getString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) ?: DEFAULT_CATEGORY
    }

    fun registerSharedPrefListener(change: Interactor.onSharedPrefChange){
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                KEY_DEFAULT_CATEGORY -> change.change()
            }
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_CATEGORY = "default_category"
        private const val DEFAULT_CATEGORY = "popular"
        private const val SHARED_FILE_NAME = "settings"
    }
}