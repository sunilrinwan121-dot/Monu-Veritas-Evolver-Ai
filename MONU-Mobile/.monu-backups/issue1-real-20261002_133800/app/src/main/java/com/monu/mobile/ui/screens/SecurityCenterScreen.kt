package com.monu.mobile.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.domain.model.MONUSecurityFinding
import com.monu.mobile.feature.security.MONUSecurityCenter

@Composable
fun SecurityCenterScreen() {

    val context: Context = LocalContext.current

    var findings by remember {
        mutableStateOf<List<MONUSecurityFinding>>(emptyList())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Security Command Center")

        Text(
            "Security status is based on verified checks where available."
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                findings = MONUSecurityCenter(context)
                    .inspect()
                    .findings
            }
        ) {
            Text("Run Security Inspection")
        }

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(findings) { finding ->
                SecurityFindingCard(finding)
            }
        }
    }
}

@Composable
private fun SecurityFindingCard(
    finding: MONUSecurityFinding
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(finding.title)
            Text(finding.description)
            Text("Category: ${finding.category}")
            Text("Status: ${finding.status}")
            Text("Verified: ${finding.verified}")
        }
    }
}
