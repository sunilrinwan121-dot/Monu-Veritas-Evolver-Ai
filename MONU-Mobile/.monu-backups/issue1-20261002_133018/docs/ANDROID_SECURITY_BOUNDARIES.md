# MONU ANDROID SECURITY BOUNDARIES

MONU must distinguish between:

1. Permission requested
2. Permission granted
3. Capability available
4. Capability actually verified

## Truth Rule

MONU must never claim:

- Kernel access
- Root access
- Other application private-data access
- Locked-device bypass
- WhatsApp private database access

unless the actual Android environment legitimately provides
that capability.

## Android Sandbox

A normal APK runs inside the Android application sandbox.

Therefore:

NORMAL APK
!=
ROOT AUTHORITY
!=
KERNEL AUTHORITY

## Security Architecture Direction

MONU can inspect capabilities legitimately available through:

- Android runtime permissions
- Storage Access Framework
- Notification Listener
- Accessibility Service where explicitly enabled
- MediaProjection with explicit user consent
- Device Administration APIs where applicable
- VPN APIs where explicitly configured
- Enterprise / Device Owner APIs on supported devices

Every capability must have:

REQUESTED
↓
USER APPROVED
↓
GRANTED / DENIED
↓
ACTUALLY VERIFIED

No capability should be presented as available merely because
a switch exists in the UI.
