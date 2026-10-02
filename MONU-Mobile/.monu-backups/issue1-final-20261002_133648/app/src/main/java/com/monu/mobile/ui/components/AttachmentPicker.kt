package com.monu.mobile.ui.components

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.domain.model.AttachmentType
import com.monu.mobile.domain.model.MONUAttachment

@Composable
fun AttachmentPicker(
    onAttachmentSelected: (MONUAttachment) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            onAttachmentSelected(
                buildAttachment(uri)
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        OutlinedButton(
            onClick = {
                launcher.launch(
                    arrayOf("image/*")
                )
            }
        ) {
            Text("📷")
        }

        OutlinedButton(
            onClick = {
                launcher.launch(
                    arrayOf("video/*")
                )
            }
        ) {
            Text("🎬")
        }

        OutlinedButton(
            onClick = {
                launcher.launch(
                    arrayOf("application/pdf")
                )
            }
        ) {
            Text("PDF")
        }

        OutlinedButton(
            onClick = {
                launcher.launch(
                    arrayOf("*/*")
                )
            }
        ) {
            Text("📎")
        }
    }
}

private fun buildAttachment(
    uri: Uri
): MONUAttachment {

    val mime = uri.toString()

    val type = when {
        mime.contains("image", ignoreCase = true) ->
            AttachmentType.IMAGE

        mime.contains("video", ignoreCase = true) ->
            AttachmentType.VIDEO

        mime.contains("pdf", ignoreCase = true) ->
            AttachmentType.PDF

        else ->
            AttachmentType.DOCUMENT
    }

    return MONUAttachment(
        uri = uri.toString(),
        name = uri.lastPathSegment ?: "Selected file",
        type = type,
        mimeType = null
    )
}
