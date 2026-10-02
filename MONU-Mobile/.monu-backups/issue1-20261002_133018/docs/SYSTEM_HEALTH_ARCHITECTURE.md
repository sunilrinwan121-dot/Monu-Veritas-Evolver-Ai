# MONU SYSTEM HEALTH ARCHITECTURE

MONU System Health is intended to report the condition of
the APK and legitimately accessible device capabilities.

Possible health areas:

- Application runtime
- Local storage
- Database
- Network
- Server connection
- WebSocket
- Offline queue
- File transfer
- Permissions
- Background services
- Security capability state

Architecture:

REAL SOURCE
↓
HEALTH ENGINE
↓
METRIC
↓
HEALTH REPORT
↓
SYSTEM HEALTH DASHBOARD

Truth Rule:

UNKNOWN is better than a false HEALTHY status.

A green status must eventually be supported by a real check.
