# MONU PERMISSION CONTROL ARCHITECTURE

MONU separates four different states:

DECLARED
↓
REQUESTED
↓
GRANTED / DENIED
↓
ACTUALLY USABLE

A permission declaration does not mean permission granted.

A granted permission does not automatically mean that a
hardware or system capability is functioning.

Truth Rule:

MONU must inspect actual permission state.

Future Permission Control Center may include:

- Permission explanations
- Request controls
- Denied capability warnings
- User action guidance
- Capability dependency mapping
