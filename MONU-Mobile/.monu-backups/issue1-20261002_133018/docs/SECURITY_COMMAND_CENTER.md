# MONU SECURITY COMMAND CENTER

The MONU Security Command Center provides visibility into
security-relevant application and device conditions.

Security categories:

- Application
- Permissions
- Network
- Server
- Storage
- Authentication
- Device
- Session

Security states:

SECURE
WARNING
RISK
CRITICAL
UNKNOWN

Truth Rule:

UNKNOWN is preferred over an invented SECURE status.

Security boundaries:

A normal Android APK must not claim:

- Root authority
- Kernel authority
- Locked-device bypass
- Private database access of other applications
- Cross-application private storage authority

unless such capability is legitimately and explicitly available.
