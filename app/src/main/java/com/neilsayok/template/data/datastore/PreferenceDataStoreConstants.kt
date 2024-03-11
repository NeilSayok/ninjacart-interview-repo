package com.neilsayok.template.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    const val SESSION_TOKEN = "SESSION_TOKEN"
    const val UUID_TOKEN = "UUID_TOKEN"
    const val IS_LOGGED_IN = "IS_LOGGED_IN"
    const val USER_PHONE = "USER_PHONE"
    const val USER_ID = "USER_ID"
    const val USER_NAME = "USER_NAME"
    const val USER_TYPE = "USER_TYPE"
    const val USER_ROLES = "USER_ROLES"
    const val USER_DEFAULT_OUTLET = "USER_DEFAULT_OUTLET"
    const val IS_CONNECTED_TO_NETWORK = "IS_CONNECTED_TO_NETWORK"


}

object PreferenceDataStoreConstants {
    val SESSION_TOKEN_KEY = stringPreferencesKey(PreferenceKeys.SESSION_TOKEN)
    val UUID_TOKEN_KEY = stringPreferencesKey(PreferenceKeys.UUID_TOKEN)
    val IS_CONNECTED_TO_NETWORK = booleanPreferencesKey(PreferenceKeys.IS_CONNECTED_TO_NETWORK)
    val IS_LOGGED_IN_KEY = booleanPreferencesKey(PreferenceKeys.IS_LOGGED_IN)
    val USER_PHONE_KEY = stringPreferencesKey(PreferenceKeys.USER_PHONE)
    val USER_ID_KEY = stringPreferencesKey(PreferenceKeys.USER_ID)
    val USER_TYPE_KEY = stringPreferencesKey(PreferenceKeys.USER_TYPE)
    val USER_ROLES_KEY = stringPreferencesKey(PreferenceKeys.USER_ROLES)
    val USER_NAME_KEY = stringPreferencesKey(PreferenceKeys.USER_NAME)
    val USER_DEFAULT_OUTLET_KEY = stringPreferencesKey(PreferenceKeys.USER_DEFAULT_OUTLET)

}

