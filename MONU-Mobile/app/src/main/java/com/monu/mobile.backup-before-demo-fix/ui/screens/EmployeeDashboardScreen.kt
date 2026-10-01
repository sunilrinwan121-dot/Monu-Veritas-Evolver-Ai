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
import com.monu.mobile.domain.model.MONUEmployee
import com.monu.mobile.domain.model.MONUEmployeeStatus
import com.monu.mobile.domain.model.MONUEmployeeType

@Composable
fun EmployeeDashboardScreen() {

    val employees = listOf(
        MONUEmployee(
            id = "developer",
            name = "Developer Employee",
            type = MONUEmployeeType.DEVELOPER,
            status = MONUEmployeeStatus.UNKNOWN,
            currentTask = "Awaiting real server data"
        ),
        MONUEmployee(
            id = "researcher",
            name = "Research Employee",
            type = MONUEmployeeType.RESEARCHER,
            status = MONUEmployeeStatus.UNKNOWN,
            currentTask = "Awaiting real server data"
        ),
        MONUEmployee(
            id = "data",
            name = "Data Employee",
            type = MONUEmployeeType.DATA_ANALYST,
            status = MONUEmployeeStatus.UNKNOWN,
            currentTask = "Awaiting real server data"
        ),
        MONUEmployee(
            id = "business",
            name = "Business Employee",
            type = MONUEmployeeType.BUSINESS,
            status = MONUEmployeeStatus.UNKNOWN,
            currentTask = "Awaiting real server data"
        ),
        MONUEmployee(
            id = "media",
            name = "Media Employee",
            type = MONUEmployeeType.MEDIA,
            status = MONUEmployeeStatus.UNKNOWN,
            currentTask = "Awaiting real server data"
        ),
        MONUEmployee(
            id = "security",
            name = "Security Employee",
            type = MONUEmployeeType.SECURITY,
            status = MONUEmployeeStatus.UNKNOWN,
            currentTask = "Awaiting real server data"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU AI Workforce")

        Text(
            "Employee states become LIVE only after real server integration."
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(employees) { employee ->
                EmployeeCard(employee)
            }
        }
    }
}

@Composable
private fun EmployeeCard(
    employee: MONUEmployee
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(employee.name)
            Text("Type: ${employee.type}")
            Text("Status: ${employee.status}")

            employee.currentTask?.let {
                Text("Current Task: $it")
            }

            employee.progress?.let {
                Text("Progress: $it%")
            }

            employee.lastActivity?.let {
                Text("Last Activity: $it")
            }

            employee.error?.let {
                Text("Error: $it")
            }
        }
    }
}
