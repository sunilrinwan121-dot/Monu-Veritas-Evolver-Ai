# MONU IDENTITY AND SESSION ARCHITECTURE

MONU distinguishes between:

1. Display identity
2. Authentication state
3. Session state
4. Server verified identity

Identity states must not be invented.

Architecture:

IDENTITY SOURCE
↓
AUTHENTICATION
↓
SESSION
↓
VERIFICATION
↓
IDENTITY CENTER

Truth Rule:

A displayed username does not automatically prove
server authentication.

UNKNOWN is preferred when no verified session exists.

Future integration:

- Server authentication
- Secure token storage
- Session refresh
- Session expiry
- Multi-device session management
- Login activity
