package com.monu.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.ui.navigation.MONUDestination

@Composable
fun MONUSidebar(
    current: MONUDestination,
    onNavigate: (MONUDestination) -> Unit,
    onNewChat: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(310.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp)
        ) {

            Text(
                text = "MONU",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Personal Command OS",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNewChat
            ) {
                Text("+ New Command")
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(MONUDestination.entries.size) { index ->
                    val destination = MONUDestination.entries[index]

                    NavigationDrawerItem(
                        label = {
                            Text("${destination.icon}  ${destination.title}")
                        },
                        selected = current == destination,
                        onClick = {
                            onNavigate(destination)
                        },
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }

            HorizontalDivider()

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "MONU Mobile Command Center",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
