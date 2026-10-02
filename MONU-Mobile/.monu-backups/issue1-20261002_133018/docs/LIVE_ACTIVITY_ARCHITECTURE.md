# MONU LIVE ACTIVITY ARCHITECTURE

MONU should not behave like a black box.

The owner should eventually be able to inspect a timeline.

Example:

10:30 Command received

10:30 Intent detected

10:31 Context analyzed

10:31 Plan generated

10:32 Developer Employee assigned

10:33 Code inspection started

10:34 Verification started

10:35 Task completed

Possible activity sources:

OWNER
MONU
SERVER
EMPLOYEE
SYSTEM
TASK
SECURITY
CONNECTION

Activity severity:

INFO
SUCCESS
WARNING
ERROR
CRITICAL

Future event transport:

Server
↓
WebSocket
↓
Realtime Event Parser
↓
Local Activity Store
↓
MONU Activity Timeline

Truth Rule:

Historical and live events should identify their real source.
