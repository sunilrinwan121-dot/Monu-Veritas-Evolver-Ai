package com.monu.mobile.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast

enum class HomeCardType {
    CHAT_ASSISTANT,
    SYSTEM_DIAGNOSTICS,
    SETTINGS_AND_BACKUP,
    TASK_MANAGER,
    OFFLINE_COMMANDS,
    REALTIME_KNOWLEDGE
}

object MONUHomeCardRouter {

    fun handleCardClick(context: Context, cardType: HomeCardType, targetActivity: Class<*>? = null) {
        if (targetActivity != null) {
            try {
                val intent = Intent(context, targetActivity)
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Opening ${cardType.name.replace('_', ' ')}...", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Action: ${cardType.name.replace('_', ' ')} triggered", Toast.LENGTH_SHORT).show()
        }
    }
}
