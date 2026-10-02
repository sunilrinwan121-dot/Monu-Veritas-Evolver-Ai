
package com.monu.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.monu.mobile.feature.workflows.MONUWorkflowScheduler
import com.monu.mobile.ui.MONUApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MONUWorkflowScheduler.ensureScheduled(applicationContext)

        setContent {
            MONUApp()
        }
    }
}
