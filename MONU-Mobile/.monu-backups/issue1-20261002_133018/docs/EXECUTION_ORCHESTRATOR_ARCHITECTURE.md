# MONU EXECUTION ORCHESTRATOR

The Execution Orchestrator manages the lifecycle between
a requested action and its actual execution state.

Architecture:

REQUEST
↓
EXECUTION CREATED
↓
QUEUED
↓
READY
↓
RUNNING
↓
TERMINAL RESULT

Possible terminal results:

- SUCCEEDED
- FAILED
- CANCELLED
- UNKNOWN

Supported execution types:

- Commands
- Workflows
- Plans
- Rules
- Tasks
- System actions

Truth Rule:

REQUESTED
!=
RUNNING
!=
SUCCEEDED

An execution status must represent the actual lifecycle state
known by the MONU system.
