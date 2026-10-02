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
import com.monu.mobile.domain.model.MONUMediaJobStatus
import com.monu.mobile.domain.model.MONUMediaOperation

@Composable
fun MediaStudioScreen() {

    val operations = listOf(
        MONUMediaOperation.GENERATE_IMAGE,
        MONUMediaOperation.EDIT_IMAGE,
        MONUMediaOperation.ENHANCE_IMAGE,
        MONUMediaOperation.REMOVE_BACKGROUND,
        MONUMediaOperation.GENERATE_VIDEO,
        MONUMediaOperation.IMAGE_TO_VIDEO,
        MONUMediaOperation.TRIM_VIDEO,
        MONUMediaOperation.CUT_VIDEO,
        MONUMediaOperation.MERGE_VIDEO,
        MONUMediaOperation.EXTRACT_FRAMES,
        MONUMediaOperation.EXTRACT_AUDIO,
        MONUMediaOperation.GENERATE_SPEECH
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Media Studio")

        Text(
            "Select a media operation. Real execution will be connected to verified server capabilities."
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(operations) { operation ->
                MediaOperationCard(operation)
            }
        }
    }
}

@Composable
private fun MediaOperationCard(
    operation: MONUMediaOperation
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(operation.name)
            Text("Status: ${MONUMediaJobStatus.UNKNOWN}")
        }
    }
}
