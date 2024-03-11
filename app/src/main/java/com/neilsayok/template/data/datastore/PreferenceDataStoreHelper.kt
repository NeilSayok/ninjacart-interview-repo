package com.neilsayok.template.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.neilsayok.template.BuildConfig
import com.neilsayok.template.data.constants.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(
    name = BuildConfig.DATA_STORE
)

class PreferenceDataStoreHelper @Inject constructor(context: Context) : IPreferenceDataStore {

    private val dataSource = context.dataStore


    override fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataSource.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val result = preferences[key] ?: defaultValue
            result
        }.distinctUntilChanged()

    override suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T): T =
        dataSource.data.first()[key] ?: defaultValue


    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataSource.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataSource.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun clearAllPreference() {
        dataSource.edit { preferences ->
            preferences.clear()
        }
    }

    override fun getSessionTokenAsFlow(): Flow<String> =
        getPreference(
            PreferenceDataStoreConstants.SESSION_TOKEN_KEY,
            AppConstants.EMPTY_STRING
        )


    override suspend fun getSessionToken(): String =
        getFirstPreference(
            PreferenceDataStoreConstants.SESSION_TOKEN_KEY,
            AppConstants.EMPTY_STRING
        )


    override suspend fun setSessionToken(sessionToken: String) {
        putPreference(PreferenceDataStoreConstants.SESSION_TOKEN_KEY, sessionToken)
    }

    override fun getUUIDAsFlow(): Flow<String> {
        return getPreference(
            PreferenceDataStoreConstants.UUID_TOKEN_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun getUUID(): String {
        return getFirstPreference(
            PreferenceDataStoreConstants.UUID_TOKEN_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun setUUIDToken(sessionToken: String) {
        putPreference(PreferenceDataStoreConstants.UUID_TOKEN_KEY, sessionToken)
    }

    override fun getIsLoggedInAsFlow(): Flow<Boolean> {
        return getPreference(PreferenceDataStoreConstants.IS_LOGGED_IN_KEY, false)
    }

    override suspend fun getIsLoggedIn(): Boolean {
        return getFirstPreference(PreferenceDataStoreConstants.IS_LOGGED_IN_KEY, false)
    }

    override suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        putPreference(PreferenceDataStoreConstants.IS_LOGGED_IN_KEY, isLoggedIn)
    }

    override fun getConnectedToNetworkAsFlow(): Flow<Boolean> {
        return getPreference(
            PreferenceDataStoreConstants.IS_CONNECTED_TO_NETWORK,
            true
        )
    }

    override suspend fun getConnectedToNetwork(): Boolean {
        return getFirstPreference(
            PreferenceDataStoreConstants.IS_CONNECTED_TO_NETWORK,
            true
        )
    }

    override suspend fun setConnectedToNetwork(isConnected: Boolean) {
        putPreference(PreferenceDataStoreConstants.IS_CONNECTED_TO_NETWORK, isConnected)
    }

    override fun getUserPhoneAsFlow(): Flow<String> {
        return getPreference(
            PreferenceDataStoreConstants.USER_PHONE_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun getUserPhone(): String {
        return getFirstPreference(
            PreferenceDataStoreConstants.USER_PHONE_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun setUserPhone(phoneNumber: String) {
        putPreference(PreferenceDataStoreConstants.USER_PHONE_KEY, phoneNumber)
    }

    override fun getUserIdAsFlow(): Flow<String> {
        return getPreference(
            PreferenceDataStoreConstants.USER_ID_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun getUserId(): String {
        return getFirstPreference(
            PreferenceDataStoreConstants.USER_ID_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun setUserId(userId: String) {
        putPreference(PreferenceDataStoreConstants.USER_ID_KEY, userId)
    }

    override fun getUserNameAsFlow(): Flow<String> {
        return getPreference(
            PreferenceDataStoreConstants.USER_NAME_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun getUserName(): String {
        return getFirstPreference(
            PreferenceDataStoreConstants.USER_NAME_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun setUserName(name: String) {
        putPreference(PreferenceDataStoreConstants.USER_NAME_KEY, name)
    }

    override fun getDefaultOutletAsFlow(): Flow<String> {
        return getPreference(
            PreferenceDataStoreConstants.USER_DEFAULT_OUTLET_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun getDefaultOutlet(): String {
        return getFirstPreference(
            PreferenceDataStoreConstants.USER_DEFAULT_OUTLET_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun setDefaultOutlet(outlets: String) {
        putPreference(PreferenceDataStoreConstants.USER_DEFAULT_OUTLET_KEY, outlets)
    }

    override fun getUserTypeAsFlow(): Flow<String> {
        return getPreference(
            PreferenceDataStoreConstants.USER_TYPE_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun getUserType(): String {
        return getFirstPreference(
            PreferenceDataStoreConstants.USER_TYPE_KEY,
            AppConstants.EMPTY_STRING
        )
    }

    override suspend fun setUserType(userType: String) {
        putPreference(PreferenceDataStoreConstants.USER_TYPE_KEY, userType)
    }


    override suspend fun getUserRoles(): List<String> {
        return getFirstPreference(
            PreferenceDataStoreConstants.USER_ROLES_KEY,
            AppConstants.EMPTY_STRING
        ).split(",")
    }

    override suspend fun setUserRoles(roles: List<String>) {
        putPreference(PreferenceDataStoreConstants.USER_ROLES_KEY, roles.joinToString(","))
    }


}