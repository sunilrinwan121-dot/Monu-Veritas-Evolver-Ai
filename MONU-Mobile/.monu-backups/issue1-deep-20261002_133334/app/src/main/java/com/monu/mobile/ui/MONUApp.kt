@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.monu.mobile.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

import com.monu.mobile.ui.components.MONUSidebar
import com.monu.mobile.ui.navigation.MONUDestination

import com.monu.mobile.ui.screens.ActivityLogScreen
import com.monu.mobile.ui.screens.ChatScreen
import com.monu.mobile.ui.screens.ConnectionScreen
import com.monu.mobile.ui.screens.DeviceCapabilityScreen
import com.monu.mobile.ui.screens.FeatureScreen
import com.monu.mobile.ui.screens.HomeScreen
import com.monu.mobile.ui.screens.MediaStudioScreen
import com.monu.mobile.ui.screens.ProjectCenterScreen
import com.monu.mobile.ui.screens.SecurityCenterScreen
import com.monu.mobile.ui.screens.ServerContractScreen
import com.monu.mobile.ui.screens.SettingsCenterScreen
import com.monu.mobile.ui.screens.TaskCenterScreen
import com.monu.mobile.ui.screens.TransferCenterScreen

@Composable
fun MONUApp() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF4FC3F7),
            secondary = Color(0xFF80CBC4),
            background = Color(0xFF0B1020),
            surface = Color(0xFF121A2B)
        )
    ) {
        MONURoot()
    }
}

@Composable
private fun MONURoot() {

    var currentDestination by remember {
        mutableStateOf(MONUDestination.HOME)
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MONUSidebar(
                current = currentDestination,

                onNavigate = { destination ->
                    currentDestination = destination

                    scope.launch {
                        drawerState.close()
                    }
                },

                onNewChat = {
                    currentDestination =
                        MONUDestination.CHAT

                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text =
                                currentDestination.title
                        )
                    },

                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Text("☰")
                        }
                    }
                )
            }
        ) { _ ->

            Surface(
                modifier =
                    Modifier.fillMaxSize()
            ) {

                when (currentDestination) {

                    MONUDestination.CONNECTION ->
                        ConnectionScreen()

                    MONUDestination.HOME ->
                        HomeScreen(
                            onOpenCommand = {
                                currentDestination =
                                    MONUDestination.CHAT
                            }
                        )

                    MONUDestination.CHAT ->
                        ChatScreen()

                    MONUDestination.TASKS ->
                        TaskCenterScreen()

                    MONUDestination.PROJECTS ->
                        ProjectCenterScreen()

                    MONUDestination.MEDIA ->
                        MediaStudioScreen()

                    MONUDestination.FILES ->
                        TransferCenterScreen()

                    MONUDestination.VOICE ->
                        FeatureScreen(
                            title = "Voice",
                            description =
                                "MONU voice intelligence module."
                        )

                    MONUDestination.SERVER ->
                        ServerContractScreen()

                    MONUDestination.SECURITY ->
                        SecurityCenterScreen()

                    MONUDestination.DEVICE ->
                        DeviceCapabilityScreen()

                    MONUDestination.ACTIVITY ->
                        ActivityLogScreen()

                    MONUDestination.SETTINGS ->
                        SettingsCenterScreen()
                }
            }
        }
    }
}
