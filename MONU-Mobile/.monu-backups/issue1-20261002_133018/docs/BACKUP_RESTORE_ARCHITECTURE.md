# MONU BACKUP AND RESTORE ARCHITECTURE

MONU backup architecture separates:

BACKUP REQUEST
↓
PREPARE
↓
SELECT SCOPE
↓
CREATE SNAPSHOT
↓
VERIFY
↓
STORE
↓
COMPLETED

Restore lifecycle:

RESTORE REQUEST
↓
SELECT BACKUP
↓
VERIFY BACKUP
↓
PREPARE
↓
RESTORE
↓
VERIFY
↓
COMPLETED

Possible backup scopes:

- Settings
- Offline commands
- Local database
- Project metadata
- User preferences

Future storage options may include:

- User-selected local document
- Application-private export
- Server backup
- Encrypted remote storage

Truth Rule:

A backup is never marked COMPLETED merely because
a backup button was pressed.

Completion requires actual successful backup creation
and verification.

Restore must never claim success without verified
restoration results.
