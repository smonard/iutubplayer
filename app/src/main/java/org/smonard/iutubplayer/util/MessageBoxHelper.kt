package org.smonard.iutubplayer.util

import android.util.Log
import android.widget.Toast
import org.smonard.iutubplayer.IutubApplication

object MessageBoxHelper {
    fun show(message: String) {
        try {
            Toast.makeText(IutubApplication.globalContext, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.i("MessageBoxHelper", e.message)
        }
    }

    fun showLongMessage(message: String) {
        try {
            Toast.makeText(IutubApplication.globalContext, message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.i("MessageBoxHelper", e.message)
        }
    }
}
