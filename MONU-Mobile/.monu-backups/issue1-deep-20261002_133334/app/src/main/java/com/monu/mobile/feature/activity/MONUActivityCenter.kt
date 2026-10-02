package com.monu.mobile.feature.activity

import com.monu.mobile.domain.model.MONUActivityLog
import com.monu.mobile.domain.model.MONUActivitySeverity
import com.monu.mobile.domain.model.MONUActivitySource

class MONUActivityCenter {

    private val activities = mutableListOf<MONUActivityLog>()

    fun addActivity(
        activity: MONUActivityLog
    ) {
        activities.add(activity)
    }

    fun getActivities(): List<MONUActivityLog> {
        return activities
            .sortedByDescending {
                it.timestamp
            }
    }

    fun getActivitiesBySource(
        source: MONUActivitySource
    ): List<MONUActivityLog> {
        return getActivities().filter {
            it.source == source
        }
    }

    fun getActivitiesBySeverity(
        severity: MONUActivitySeverity
    ): List<MONUActivityLog> {
        return getActivities().filter {
            it.severity == severity
        }
    }

    fun clear() {
        activities.clear()
    }
}
