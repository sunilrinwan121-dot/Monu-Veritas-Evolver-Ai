# MONU Service Coordination

## Purpose

The Service Coordinator provides an architecture for coordinating
registered MONU services.

## Service Lifecycle

UNKNOWN -> REGISTERED -> STARTING -> RUNNING

STOPPED and FAILED remain explicit states.

## Security Boundary

A normal Android application cannot assume authority over arbitrary
system services, private services, or other applications.

## Truth Rule

Declared service availability is not equivalent to verified execution.
