package ru.araok.data

import android.content.Context
import android.content.Context.MODE_PRIVATE

private const val PREFERENCE_NAME = "store"
private const val ACCESS_TOKEN = "accessToken"
private const val REFRESH_TOKEN = "refreshToken"

object Repository {
    private var localAccessToken: String? = null
    private var localRefreshToken: String? = null

    fun getAccessToken(context: Context): String {
        return localAccessToken ?: getAccessTokenFromSharedPreference(context) ?: ""
    }

    fun getRefreshToken(context: Context): String {
        return localRefreshToken ?: getRefreshTokenFromSharedPreference(context) ?: ""
    }

    fun saveAccessToken(context: Context, accessToken: String) {
        localAccessToken = accessToken

        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString(ACCESS_TOKEN, accessToken)
        edit.apply()
    }

    fun saveRefreshToken(context: Context, refreshToken: String) {
        localRefreshToken = refreshToken

        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString(REFRESH_TOKEN, refreshToken)
        edit.apply()
    }

    private fun getAccessTokenFromSharedPreference(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        return prefs.getString(ACCESS_TOKEN, null)
    }

    private fun getRefreshTokenFromSharedPreference(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        return prefs.getString(REFRESH_TOKEN, null)
    }
}