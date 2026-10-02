package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeCustomizationScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Home Personalization")

        Text(
            "Future personalization options:"
        )

        Text("• Default MONU theme")
        Text("• Custom colors")
        Text("• Personal photo background")
        Text("• Generated AI background")
        Text("• Mood-based appearance")
        Text("• Server-generated visual themes")
        Text("• Dynamic home cards")

        Button(
            onClick = { }
        ) {
            Text("Background Configuration")
        }
    }
}
