package com.jasz.ultrapdf.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferencesKeys {
        val IS_PREMIUM = booleanPreferencesKey("is_premium")
        val IS_OCR_UNLOCKED = booleanPreferencesKey("is_ocr_unlocked")
    }

    val isPremium: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_PREMIUM] ?: false
        }

    val isOcrUnlocked: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_OCR_UNLOCKED] ?: false
        }

    suspend fun setPremium(isPremium: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.IS_PREMIUM] = isPremium
        }
    }

    suspend fun setOcrUnlocked(isOcrUnlocked: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.IS_OCR_UNLOCKED] = isOcrUnlocked
        }
    }
}
