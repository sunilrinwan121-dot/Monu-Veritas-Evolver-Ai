package com.monu.mobile.feature.notification

import com.monu.mobile.domain.model.MONUNotification
import com.monu.mobile.domain.model.MONUNotificationPriority
import com.monu.mobile.domain.model.MONUNotificationSource
import java.util.UUID

class MONUNotificationCenter {

    private val notifications = mutableListOf<MONUNotification>()

    fun add(
        source: MONUNotificationSource,
        priority: MONUNotificationPriority,
        title: String,
        message: String
    ): MONUNotification {

        val notification = MONUNotification(
            id = UUID.randomUUID().toString(),
            source = source,
            priority = priority,
            title = title,
            message = message
        )

        notifications.add(0, notification)

        return notification
    }

    fun all(): List<MONUNotification> {
        return notifications.toList()
    }

    fun unreadCount(): Int {
        return notifications.count { !it.read }
    }

    fun bySource(
        source: MONUNotificationSource
    ): List<MONUNotification> {
        return notifications.filter {
            it.source == source
        }
    }
}
