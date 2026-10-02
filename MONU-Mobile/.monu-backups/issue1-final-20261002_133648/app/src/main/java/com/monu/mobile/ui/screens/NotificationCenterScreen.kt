package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.domain.model.MONUNotification
import com.monu.mobile.domain.model.MONUNotificationPriority
import com.monu.mobile.domain.model.MONUNotificationSource

@Composable
fun NotificationCenterScreen() {

    val demoNotifications = listOf(
        MONUNotification(
            id = "server",
            source = MONUNotificationSource.SERVER,
            priority = MONUNotificationPriority.NORMAL,
            title = "Server Notifications",
            message = "Real server notifications will appear here."
        ),
        MONUNotification(
            id = "heartbeat",
            source = MONUNotificationSource.HEARTBEAT,
            priority = MONUNotificationPriority.NORMAL,
            title = "Heartbeat Updates",
            message = "Real heartbeat results will appear here."
        ),
        MONUNotification(
            id = "mobile",
            source = MONUNotificationSource.MOBILE,
            priority = MONUNotificationPriority.NORMAL,
            title = "Mobile Notifications",
            message = "MONU mobile system events will appear here."
        ),
        MONUNotification(
            id = "task",
            source = MONUNotificationSource.TASK,
            priority = MONUNotificationPriority.HIGH,
            title = "Task Updates",
            message = "Real task progress and completion events will appear here."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Notification Center")

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(demoNotifications) { notification ->
                NotificationCard(notification)
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: MONUNotification
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(notification.title)
            Text(notification.message)
            Text("Source: ${notification.source}")
            Text("Priority: ${notification.priority}")
        }
    }
}
