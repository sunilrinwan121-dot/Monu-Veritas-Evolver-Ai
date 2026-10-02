# MONU System Recovery Architecture

## Purpose
Provide architecture for failure detection, checkpoints, recovery planning,
recovery execution and verification.

## Truth Rules
- Failure detection does not imply recovery.
- Planned recovery does not imply execution.
- Executed recovery does not imply verified recovery.
- Recovery evidence must originate from real system events.
