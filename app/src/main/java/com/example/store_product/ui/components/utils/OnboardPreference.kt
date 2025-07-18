package com.example.store_product.ui.components.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


object OnBoardingPreferences {
    private val Context.dataStore by preferencesDataStore(name = "onboarding_prefs")

    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    suspend fun setCompleted(context: Context, completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = completed
        }
    }

    val completedFlow: (Context) -> Flow<Boolean> = { context ->
        context.dataStore.data.map { prefs ->
            prefs[ONBOARDING_COMPLETED] ?: false
        }
    }
}