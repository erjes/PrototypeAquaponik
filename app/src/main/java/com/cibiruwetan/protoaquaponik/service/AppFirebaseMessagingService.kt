package com.cibiruwetan.protoaquaponik.service

import com.cibiruwetan.protoaquaponik.R
import com.google.firebase.Firebase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"]
            ?: remoteMessage.data["judul"]
            ?: remoteMessage.notification?.title
            ?: getString(R.string.notification_default_title)

        val message = remoteMessage.data["body"]
            ?: remoteMessage.data["message"]
            ?: remoteMessage.data["pesan"]
            ?: remoteMessage.notification?.body
            ?: getString(R.string.notification_default_body)

        NotificationHelper.showPondAlert(this, title, message)
    }

    override fun onNewToken(token: String) {
        val tokenPayload = mapOf(
            "token" to token,
            "updatedAt" to ServerValue.TIMESTAMP
        )
        Firebase.database
            .getReference("fcm_tokens")
            .child(token.toFirebaseKey())
            .setValue(tokenPayload)
    }
}

private fun String.toFirebaseKey(): String {
    return replace(".", "_")
        .replace("#", "_")
        .replace("$", "_")
        .replace("[", "_")
        .replace("]", "_")
        .replace("/", "_")
}
