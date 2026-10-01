package com.monu.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.domain.model.MONUAttachment

@Composable
fun CommandInput(
    placeholder: String = "Tell MONU what to do...",
    onSend: (String, List<MONUAttachment>) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var attachments by remember {
        mutableStateOf<List<MONUAttachment>>(emptyList())
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        if (attachments.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = "Attached: ${attachments.joinToString { it.name }}"
            )
        }

        AttachmentPicker { attachment ->
            attachments = attachments + attachment
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(placeholder)
                },
                maxLines = 4
            )

            Button(
                modifier = Modifier.height(56.dp),
                enabled = text.isNotBlank() || attachments.isNotEmpty(),
                onClick = {
                    onSend(
                        text.trim(),
                        attachments
                    )

                    text = ""
                    attachments = emptyList()
                }
            ) {
                Text("➤")
            }
        }
    }
}
