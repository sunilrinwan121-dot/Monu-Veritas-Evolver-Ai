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
import com.monu.mobile.feature.knowledge.MONUKnowledgeCenter

@Composable
fun KnowledgeCenterScreen() {

    val knowledge = MONUKnowledgeCenter().demoKnowledge()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("MONU Knowledge Center")

        Text(
            "Knowledge is designed to originate from real documents, projects, conversations and explicitly stored information."
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(knowledge) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(item.title)
                        Text(item.content)
                        Text("Source: ${item.source}")
                        Text("Status: ${item.status}")
                        Text("Tags: ${item.tags.joinToString()}")
                    }
                }
            }
        }
    }
}
