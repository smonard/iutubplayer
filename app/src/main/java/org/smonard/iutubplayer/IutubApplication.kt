package org.smonard.iutubplayer

import android.app.Application
import android.content.Context

open class IutubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        globalContext = applicationContext
    }

    companion object {
        var globalContext: Context? = null
            private set
        var currentPlayingContext = R.id.related
    }
}