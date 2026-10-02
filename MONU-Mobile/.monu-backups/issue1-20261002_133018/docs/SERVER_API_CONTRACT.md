# MONU MOBILE <-> MONU SERVER CONTRACT

This document intentionally separates:

1. Known endpoints
2. Configured endpoints
3. Verified endpoints

## APK -> Server

The APK may eventually use:

- Health endpoint
- Capabilities endpoint
- Chat endpoint
- Command endpoint
- Upload endpoint
- WebSocket endpoint

## Truth Rules

An endpoint is not considered available merely because:

- A URL was entered
- A UI screen exists
- A button exists
- A test string exists

It becomes VERIFIED only after a real request succeeds.

## WebSocket

CONNECTED only after:

WebSocketListener.onOpen()

FAILED after:

WebSocketListener.onFailure()

## Server Contract Discovery

Before production integration, inspect the real MONU Server routes.

Never invent endpoint names if the server already defines them.
