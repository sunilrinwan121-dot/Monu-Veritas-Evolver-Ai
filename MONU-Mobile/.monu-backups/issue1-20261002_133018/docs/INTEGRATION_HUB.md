# MONU Integration Hub

## Purpose

The Integration Hub provides a boundary-aware architecture for
representing external and internal integrations.

## Integration Lifecycle

UNKNOWN -> DISCOVERED -> CONFIGURED -> CONNECTING -> CONNECTED

Failure and disconnection remain explicitly represented.

## Truth Rule

An integration is never treated as connected merely because an endpoint
definition exists.

Real connection state requires real transport verification.
