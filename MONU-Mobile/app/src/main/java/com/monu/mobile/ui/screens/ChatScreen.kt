package com.monu.mobile.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.monu.mobile.core.network.MONUNetworkMonitor
import com.monu.mobile.domain.model.ChatMessage
import com.monu.mobile.domain.model.InternetKnowledgeResult
import com.monu.mobile.domain.model.InternetKnowledgeState
import com.monu.mobile.domain.model.MessageRole
import com.monu.mobile.feature.intelligence.MONUMasterBrain
import com.monu.mobile.feature.conversation.MONUChatScreenIntegration
import com.monu.mobile.feature.conversation.MONUConversationChatMapper
import com.monu.mobile.feature.voice.MONUVoiceEngine
import com.monu.mobile.feature.voice.MONUVoiceInputEngine
import com.monu.mobile.ui.components.CommandInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun ChatScreen() {
val context = LocalContext.current
val scope = rememberCoroutineScope()


val masterBrain = remember {
    MONUMasterBrain(
        networkMonitor = MONUNetworkMonitor(
            context.applicationContext
        )
    )
}

val conversationIntegration = remember(masterBrain) {
    MONUChatScreenIntegration(masterBrain)
}

var conversationVersion by remember {
    mutableStateOf(0)
}

fun refreshConversation() {
    conversationVersion++
}

fun conversationMessages(): List<ChatMessage> {
    conversationVersion

    return MONUConversationChatMapper.toChatMessages(
        conversationIntegration.messages()
    )
}

var voiceReady by remember {
    mutableStateOf(false)
}

var searching by remember {
    mutableStateOf(false)
}

var userFacingError by remember {
    mutableStateOf<String?>(null)
}

var lastFailedCommand by remember {
    mutableStateOf("")
}

var voiceListening by remember {
    mutableStateOf(false)
}

var voiceInputStatus by remember {
    mutableStateOf("")
}

var voiceRuntimeHealth by remember {
    mutableStateOf("Initializing")
}

val messages = conversationMessages()


fun submitToMasterBrain(
    command: String
) {
    val cleanCommand = command.trim()

    if (cleanCommand.isBlank()) {
        return
    }

    scope.launch {
        searching = true

        try {
            val submission =
                conversationIntegration.submit(cleanCommand)

            val failed =
                submission.result?.masterBrainResult?.success == false

            if (failed) {
                userFacingError =
                    submission.state.errorMessage
                        ?: submission.result?.masterBrainResult?.text
                        ?: "MONU could not complete this request."

                lastFailedCommand = cleanCommand
            } else {
                userFacingError = null
                lastFailedCommand = ""
            }

            refreshConversation()
        } catch (error: Exception) {
            userFacingError =
                mapUserFacingError(error)

            lastFailedCommand = cleanCommand
            refreshConversation()
        } finally {
            searching = false
        }
    }
}

val voiceEngine = remember {
    MONUVoiceEngine(context) {
        voiceReady = it
    }
}

val voiceInputEngine = remember {
    MONUVoiceInputEngine(
        context = context,
        onResult = { recognizedCommand ->
            val cleanCommand = recognizedCommand.trim()

            if (cleanCommand.isBlank()) {
                return@MONUVoiceInputEngine
            }

            submitToMasterBrain(cleanCommand)
        },
        onError = { error ->
            voiceInputStatus = error
            voiceListening = false
        },
        onListeningStateChanged = { isListening ->
            voiceListening = isListening
        }
    )
}

DisposableEffect(Unit) {
    voiceRuntimeHealth =
        voiceInputEngine.getRuntimeHealth()

    onDispose {
        voiceEngine.shutdown()
        voiceInputEngine.shutdown()
    }
}

val microphonePermissionLauncher =
    rememberLauncherForActivityResult(
        contract =
            ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            voiceInputStatus = ""
            voiceInputEngine.startListening()
            voiceRuntimeHealth =
                voiceInputEngine.getRuntimeHealth()
        } else {
            voiceInputStatus =
                "Microphone permission was denied."
            voiceListening = false
            voiceRuntimeHealth =
                voiceInputEngine.getRuntimeHealth()
        }
    }

Column(
    modifier = Modifier.fillMaxSize()
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = "COMMAND CENTER",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Text(
            text =
                when {
                    searching ->
                        "SEARCHING INTERNET"

                    voiceReady ->
                        "VOICE READY • INTERNET READY"

                    else ->
                        "VOICE INITIALIZING • INTERNET READY"
                },
            style =
                MaterialTheme.typography.labelSmall
        )
    }

    userFacingError?.let { message ->
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "MONU could not complete the request",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (lastFailedCommand.isNotBlank()) {
                        OutlinedButton(
                            onClick = {
                                val retryCommand = lastFailedCommand
                                userFacingError = null
                                submitToMasterBrain(retryCommand)
                            }
                        ) {
                            Text("Retry")
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            userFacingError = null
                            lastFailedCommand = ""
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier =
            Modifier
                .weight(1f)
                .fillMaxWidth(),
        contentPadding =
            PaddingValues(12.dp),
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = messages,
            key = { it.id }
        ) { message ->
            MessageCard(
                message = message,
                onCopy = {
                    copyToClipboard(
                        context,
                        message.content
                    )
                },
                onListen = {
                    voiceEngine.speak(message.content)
                },
                onStop = {
                    voiceEngine.stop()
                },
                onShare = {
                    shareText(
                        context,
                        message.content
                    )
                }
            )
        }

        if (searching) {
            item {
                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier =
                            Modifier.padding(16.dp),
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(24.dp)
                        )

                        Text(
                            text =
                                "MONU is retrieving information from the internet..."
                        )
                    }
                }
            }
        }
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {
                if (voiceListening) {
                    voiceInputEngine.stopListening()
                } else {
                    voiceInputStatus = ""

                    val microphoneGranted =
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) ==
                            PackageManager.PERMISSION_GRANTED

                    if (microphoneGranted) {
                        voiceInputEngine.startListening()
                    } else {
                        microphonePermissionLauncher.launch(
                            Manifest.permission.RECORD_AUDIO
                        )
                    }
                }

                voiceRuntimeHealth =
                    voiceInputEngine.getRuntimeHealth()
            }
        ) {
            Text(
                if (voiceListening) {
                    "Stop Listening"
                } else {
                    "Start Voice"
                }
            )
        }

        Column {
            Text(
                text = voiceRuntimeHealth,
                style =
                    MaterialTheme.typography.labelSmall
            )

            if (voiceInputStatus.isNotBlank()) {
                Text(
                    text = voiceInputStatus,
                    style =
                        MaterialTheme.typography.labelSmall
                )
            }
        }
    }

    CommandInput { command, attachments ->
        val cleanCommand = command.trim()

        val attachmentText =
            if (attachments.isEmpty()) {
                ""
            } else {
                "\nAttachments selected: " +
                    attachments.joinToString {
                        it.name
                    }
            }

        if (cleanCommand.isBlank()) {
            if (attachments.isNotEmpty()) {
                submitToMasterBrain(
                    "Attachments selected, but no verified attachment analysis provider is connected." +
                        attachmentText
                )
            } else {
                return@CommandInput
            }

            return@CommandInput
        }

        submitToMasterBrain(
            cleanCommand + attachmentText
        )
    }
}

}


private fun mapUserFacingError(
    error: Throwable
): String {
    return when (error) {
        is java.net.UnknownHostException ->
            "No internet connection or the requested service could not be reached."

        is java.net.SocketTimeoutException ->
            "The request took too long to complete. Please check your connection and try again."

        is java.io.IOException ->
            "A network error prevented MONU from completing the request."

        is SecurityException ->
            "MONU does not have permission required to complete this request."

        else ->
            "MONU could not complete the request. Please try again."
    }
}

@Composable
fun MessageCard(
message: ChatMessage,
onCopy: () -> Unit,
onListen: () -> Unit,
onStop: () -> Unit,
onShare: () -> Unit
) {
Card(
modifier = Modifier.fillMaxWidth()
) {
Column(
modifier = Modifier.padding(14.dp)
) {
val roleName =
when (message.role) {
MessageRole.OWNER -> "YOU"
MessageRole.MONU -> "MONU"
MessageRole.SYSTEM -> "SYSTEM"
}

        Text(
            text = roleName,
            style =
                MaterialTheme.typography.labelMedium
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text = message.content,
            style =
                MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {
            OutlinedButton(
                onClick = onCopy
            ) {
                Text("Copy")
            }

            OutlinedButton(
                onClick = onListen
            ) {
                Text("Listen")
            }

            OutlinedButton(
                onClick = onStop
            ) {
                Text("Stop")
            }

            OutlinedButton(
                onClick = onShare
            ) {
                Text("Share")
            }
        }
    }
}

}

private fun copyToClipboard(
context: Context,
text: String
) {
val clipboard =
context.getSystemService(
Context.CLIPBOARD_SERVICE
) as ClipboardManager

clipboard.setPrimaryClip(
    ClipData.newPlainText(
        "MONU Message",
        text
    )
)

}

private fun shareText(
context: Context,
text: String
) {
val intent =
Intent(Intent.ACTION_SEND).apply {
type = "text/plain"
putExtra(
Intent.EXTRA_TEXT,
text
)
}

context.startActivity(
    Intent.createChooser(
        intent,
        "Share MONU Message"
    )
)

}
