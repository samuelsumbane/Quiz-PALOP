package com.samuelsumbane.quizpalop.domain.repository
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SettingsManager(val context: Context) {

    val Context.dataStore by preferencesDataStore(name = "settings")
    val userCoins = intPreferencesKey("user_coins")
    val lives = intPreferencesKey("user_lives")
    val playSound = booleanPreferencesKey("play_sound")
    val vibrateOnTap = booleanPreferencesKey("vibrateontap")

    val savedQuestionsList = stringPreferencesKey("savedQuestions_list")
    val lastSelectedCategory = stringPreferencesKey("lastCategorySelected")
    val lastSelectedCountry = stringPreferencesKey("lastCountrySelected")

    val lastRightOptionButtonDateTime = longPreferencesKey("last_datetime_rightOption_clicked")
    val lastDateTimeLostLives = longPreferencesKey("last_datetime_lost_lives")
    val savedDailyQuestions = stringPreferencesKey("savedDailyQuestions")
    val actualDailyQuestionId = stringPreferencesKey("actualDailyQuestionId")
//    val answeredDailyQuestionDateTime = longPreferencesKey("answeredDailyQuestionDateTime")
    val lastDateTimeUserGotDailyQuestionId = longPreferencesKey("lastDateTimeUserGotDailyQuestionId")

    /**
     * Reads Int DataStore values
     */
    fun readIntValues(value: Preferences.Key<Int>): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[value] ?: 0
        }
    }

    suspend fun saveIntValues(valueName: Preferences.Key<Int>, intValue: Int) {
        context.dataStore.edit { pref ->
            pref[valueName] = intValue
        }
    }

    suspend fun readLongValues(valueName: Preferences.Key<Long>): Long {
        val prefs = context.dataStore.data.first()
        return prefs[valueName] ?: 0L
    }

    suspend fun saveLongValue(valueName: Preferences.Key<Long>, longValue: Long) {
        context.dataStore.edit { pref ->
            pref[valueName] = longValue
        }
    }

    fun readStringValues(value: Preferences.Key<String>): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[value] ?: ""
        }
    }

    suspend fun saveStringValues(valueName: Preferences.Key<String>, stringValue: String) {
        context.dataStore.edit { pref ->
            pref[valueName] = stringValue
        }
    }

    //
    fun readBooleanValue(value: Preferences.Key<Boolean>): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[value] ?: (value == playSound)
        }
    }

    suspend fun saveBooleanValues(valueName: Preferences.Key<Boolean>, booleanValue: Boolean) {
        context.dataStore.edit { pref ->
            pref[valueName] = booleanValue
        }
    }

    fun readSavedStringsValues(dataList: Preferences.Key<String>): Flow<Set<String>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[dataList] ?: "[]"
            Json.decodeFromString<Set<String>>(json)
        }
    }

    suspend fun saveStringsValues(dataList: Preferences.Key<String>, listValue: Set<String>) {
        val jsonData = Json.encodeToString(listValue)
        context.dataStore.edit { pref ->
            pref[dataList] = jsonData
        }
    }

}
