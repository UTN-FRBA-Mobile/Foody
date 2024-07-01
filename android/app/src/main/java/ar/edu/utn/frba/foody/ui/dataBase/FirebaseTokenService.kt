package ar.edu.utn.frba.foody.ui.dataBase

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseTokenService(private val context: Context) {
    private val TAG = "FirebaseTokenManager"

    fun getAndSaveToken() {
        FirebaseMessaging.getInstance().getToken()
            .addOnSuccessListener { token ->
                if (!token.isNullOrEmpty()) {
                    storeToken(token)
                    Log.d(TAG, "Token obtenido: $token")
                } else {
                    Log.w(TAG, "Error al obtener token")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al obtener token", e)
            }
    }

    fun getTokenFromPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("firebase_token", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "")
    }

    private fun storeToken(token: String) {
        val sharedPreferences = context.getSharedPreferences("firebase_token", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }
}