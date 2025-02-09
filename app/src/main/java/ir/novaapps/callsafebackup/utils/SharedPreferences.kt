package ir.novaapps.callsafebackup.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(
    val context : Context
) {
    private val detailApp: SharedPreferences =
        context.getSharedPreferences("detailApp", Context.MODE_PRIVATE)

    fun setDetailString(key: String, value: String) {
        detailApp.edit().putString(key, value).apply()
    }
    fun getDetailString(key: String , default:String): String? {
        return detailApp.getString(key, default)
    }

    fun setDetailBoolean(key: String, value: Boolean) {
        detailApp.edit().putBoolean(key, value).apply()
    }
    fun getDetailBoolean(key: String , default:Boolean): Boolean {
        return detailApp.getBoolean(key, default)
    }

}