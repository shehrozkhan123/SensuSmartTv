package com.google.android.apps.sensusmarttv

import android.app.Application
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.service.DIALService

class AppConfig : Application(){
    override fun onCreate() {
        DIALService.registerApp("Levak");
        DiscoveryManager.init(applicationContext);
        super.onCreate()
    }
}