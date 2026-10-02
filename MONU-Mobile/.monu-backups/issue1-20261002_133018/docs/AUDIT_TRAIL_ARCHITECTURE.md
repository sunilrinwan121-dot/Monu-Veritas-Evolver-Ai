# MONU AUDIT TRAIL ARCHITECTURE

The MONU Audit Trail is designed to provide a chronological
record of important MONU actions.

Possible actors:

OWNER
MONU
SERVER
EMPLOYEE
SYSTEM
UNKNOWN

Possible actions:

- Command received
- Command executed
- Task created
- Task completed
- Task failed
- Login
- Logout
- Configuration changed
- Permission changed
- Security event
- File transfer
- Backup
- Restore

Architecture:

REAL EVENT
↓
AUDIT RECORDER
↓
AUDIT ENTRY
↓
PERSISTENT STORE
↓
AUDIT TIMELINE

Truth Rule:

An audit record must not falsely imply that an action occurred.

Future production features:

- Persistent audit database
- Search
- Filters
- Export
- Server synchronization
- Tamper-evidence
- Security event correlation
