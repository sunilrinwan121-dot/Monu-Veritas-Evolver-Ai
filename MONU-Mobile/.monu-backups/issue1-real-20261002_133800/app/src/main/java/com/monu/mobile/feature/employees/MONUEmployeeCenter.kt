package com.monu.mobile.feature.employees

import com.monu.mobile.domain.model.MONUEmployee
import com.monu.mobile.domain.model.MONUEmployeeStatus
import com.monu.mobile.domain.model.MONUEmployeeType

class MONUEmployeeCenter {

    private val employees = mutableListOf<MONUEmployee>()

    fun replaceEmployees(
        newEmployees: List<MONUEmployee>
    ) {
        employees.clear()
        employees.addAll(newEmployees)
    }

    fun getEmployees(): List<MONUEmployee> {
        return employees.toList()
    }

    fun getEmployee(
        employeeId: String
    ): MONUEmployee? {
        return employees.find {
            it.id == employeeId
        }
    }

    fun updateEmployeeStatus(
        employeeId: String,
        status: MONUEmployeeStatus,
        task: String? = null,
        progress: Int? = null
    ) {
        val index = employees.indexOfFirst {
            it.id == employeeId
        }

        if (index >= 0) {
            val old = employees[index]

            employees[index] = old.copy(
                status = status,
                currentTask = task ?: old.currentTask,
                progress = progress ?: old.progress
            )
        }
    }

    fun countByStatus(
        status: MONUEmployeeStatus
    ): Int {
        return employees.count {
            it.status == status
        }
    }

    fun countByType(
        type: MONUEmployeeType
    ): Int {
        return employees.count {
            it.type == type
        }
    }
}
