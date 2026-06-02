package com.cibiruwetan.protoaquaponik

import android.app.Application
import com.cibiruwetan.protoaquaponik.service.NotificationHelper

class ProtoAquaponikApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.ensureAlertChannel(this)
    }
}
