package com.monu.mobile.feature.events

import com.monu.mobile.domain.model.EventInsight
import com.monu.mobile.domain.model.EventReport
import com.monu.mobile.domain.model.MONUEvent
import com.monu.mobile.domain.model.MONUEventStatus

class MONUEventIntelligenceHub {

    private val events = mutableListOf<MONUEvent>()

    fun publish(event: MONUEvent): MONUEvent {
        events.removeAll { it.eventId == event.eventId }
        events += event
        return event
    }

    fun updateStatus(
        eventId: String,
        status: MONUEventStatus
    ): MONUEvent? {

        val index = events.indexOfFirst {
            it.eventId == eventId
        }

        if (index == -1) return null

        val updated = events[index].copy(
            status = status
        )

        events[index] = updated

        return updated
    }

    fun getEvents(): List<MONUEvent> {
        return events.sortedByDescending {
            it.timestamp
        }
    }

    fun analyze(eventId: String): EventInsight? {

        val event = events.firstOrNull {
            it.eventId == eventId
        } ?: return null

        return EventInsight(
            eventId = event.eventId,
            category = event.type.name,
            summary = "${event.type} event: ${event.title}",
            confidence = if (
                event.status == MONUEventStatus.PROCESSED
            ) 100 else 0
        )
    }

    fun report(): EventReport {
        return EventReport(
            events = getEvents(),
            total = events.size,
            received = events.count {
                it.status == MONUEventStatus.RECEIVED
            },
            processed = events.count {
                it.status == MONUEventStatus.PROCESSED
            },
            failed = events.count {
                it.status == MONUEventStatus.FAILED
            },
            unknown = events.count {
                it.status == MONUEventStatus.UNKNOWN
            }
        )
    }
}
