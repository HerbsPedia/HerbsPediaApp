package id.zamzam.herbspedia.data.pref

import android.content.Context
import android.content.SharedPreferences

class UserPreferences private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val SHARED_PREF_NAME = "user_session"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "name"
        private const val KEY_PASSWORD = "password"
        private const val KEY_LOGGED_IN = "logged_in"

        @Volatile
        private var instance: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return instance ?: synchronized(this) {
                instance ?: UserPreferences(context).also { instance = it }
            }
        }
    }

    fun saveUserSession(email: String, name: String, password: String) {
        sharedPreferences.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_NAME, name)
            putString(KEY_PASSWORD, password)
            putBoolean(KEY_LOGGED_IN, true)
            apply()
        }
    }

    fun clearUserSession() {
        sharedPreferences.edit().apply {
            remove(KEY_EMAIL)
            remove(KEY_NAME)
            remove(KEY_PASSWORD)
            putBoolean(KEY_LOGGED_IN, false)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_NAME, null)
    }

    fun getUserPassword(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

}