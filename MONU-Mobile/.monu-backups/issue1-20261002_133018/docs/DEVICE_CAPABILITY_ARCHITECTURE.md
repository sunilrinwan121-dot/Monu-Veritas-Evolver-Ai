# MONU DEVICE CAPABILITY INSPECTOR

MONU can inspect legitimately available device features.

Possible capability areas:

- Camera hardware
- Microphone hardware
- Application storage
- Network transport
- Notification capability
- Media selection APIs
- Text-to-Speech
- Bluetooth where permission allows
- Location where permission allows

Capability states:

AVAILABLE
UNAVAILABLE
REQUIRES_PERMISSION
REQUIRES_USER_ACTION
UNKNOWN

Truth Rule:

Hardware presence
!=
Permission granted
!=
Capability verified

Root authority is never assumed.

Kernel authority is never assumed.

Other application private data access is never assumed.
