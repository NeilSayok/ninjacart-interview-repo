package com.neilsayok.template.data.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface IPreferenceDataStore {

    //General Getter Setters
    fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T): T
    suspend fun <T> putPreference(key: Preferences.Key<T>, value: T)
    suspend fun <T> removePreference(key: Preferences.Key<T>)
    suspend fun clearAllPreference()

    //Session Preferences
    fun getSessionTokenAsFlow(): Flow<String>
    suspend fun getSessionToken(): String
    suspend fun setSessionToken(sessionToken: String)

    //User Unique ID Preferences
    fun getUUIDAsFlow(): Flow<String>
    suspend fun getUUID(): String
    suspend fun setUUIDToken(sessionToken: String)

    fun getIsLoggedInAsFlow(): Flow<Boolean>
    suspend fun getIsLoggedIn(): Boolean
    suspend fun setIsLoggedIn(isLoggedIn: Boolean)

    fun getConnectedToNetworkAsFlow(): Flow<Boolean>
    suspend fun getConnectedToNetwork(): Boolean
    suspend fun setConnectedToNetwork(isConnected: Boolean)

    fun getUserPhoneAsFlow(): Flow<String>
    suspend fun getUserPhone(): String
    suspend fun setUserPhone(phoneNumber: String)

    fun getUserIdAsFlow(): Flow<String>
    suspend fun getUserId(): String
    suspend fun setUserId(userId: String)
    fun getUserNameAsFlow(): Flow<String>
    suspend fun getUserName(): String
    suspend fun setUserName(name: String)

    fun getUserTypeAsFlow(): Flow<String>
    suspend fun getUserType(): String
    suspend fun setUserType(userType: String)

    suspend fun getUserRoles(): List<String>
    suspend fun setUserRoles(roles: List<String>)

    fun getDefaultOutletAsFlow(): Flow<String>
    suspend fun getDefaultOutlet(): String
    suspend fun setDefaultOutlet(outlets: String)
}