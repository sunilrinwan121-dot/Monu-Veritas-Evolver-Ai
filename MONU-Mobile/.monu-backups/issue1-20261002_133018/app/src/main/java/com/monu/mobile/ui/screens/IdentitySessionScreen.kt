package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.identity.MONUIdentityCenter

@Composable
fun IdentitySessionScreen() {

    val center = remember { MONUIdentityCenter() }

    var identityText by remember {
        mutableStateOf("Identity information not yet inspected.")
    }

    var sessionText by remember {
        mutableStateOf("Session information not yet inspected.")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Identity & Session Center")

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                val identity = center.currentIdentity()
                val session = center.currentSession()

                identityText =
                    "Identity: ${identity.displayName}\n" +
                    "Authenticated: ${identity.authenticated}\n" +
                    "Verified: ${identity.verified}\n" +
                    "Source: ${identity.source}"

                sessionText =
                    "Session: ${session.status}\n" +
                    "Verified: ${session.verified}"
            }
        ) {
            Text("Inspect Identity")
        }

        Card(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(identityText)
            }
        }

        Card(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(sessionText)
            }
        }
    }
}
