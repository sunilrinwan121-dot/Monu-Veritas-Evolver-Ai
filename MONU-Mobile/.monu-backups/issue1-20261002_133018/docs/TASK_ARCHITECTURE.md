# MONU TASK CENTER ARCHITECTURE

Every MONU task follows a lifecycle.

QUEUED
↓
STARTING
↓
RUNNING
↓
PROCESSING
↓
VERIFYING
↓
COMPLETED

Failure lifecycle:

FAILED
↓
DIAGNOSING
↓
RECOVERING
↓
RETRYING

Future integrations:

- Server task events
- WebSocket progress
- Background workers
- Task cancellation
- Retry controls
- Activity timeline
- Result attachments

Truth Rule:

Task progress must eventually originate from real task events.
