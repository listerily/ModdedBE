package net.listerily.moddedbe

import android.app.Application
import net.listerily.endercore.android.EnderCore

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EnderCore.instance.initialize(this, EnderCore.MODE_PUBLIC)
    }
}