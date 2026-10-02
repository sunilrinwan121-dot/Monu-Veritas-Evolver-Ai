# MONU MEDIA STUDIO ARCHITECTURE

MONU Mobile acts as the Media Command Center.

Possible operations:

## IMAGE

- Generate
- Edit
- Enhance
- Resize
- Crop
- Remove background
- Convert

## VIDEO

- Generate
- Image to video
- Video to image
- Trim
- Cut
- Merge
- Extract frames
- Compress
- Convert

## AUDIO

- Extract audio
- Convert audio
- Voice processing
- Generate speech

Architecture:

SELECT
↓
ATTACH
↓
UPLOAD
↓
CREATE JOB
↓
SERVER PROCESSING
↓
REALTIME PROGRESS
↓
VERIFY
↓
PREVIEW
↓
DOWNLOAD RESULT

The Media Studio is designed as a universal command layer.

Future capability discovery may determine which operations
are actually supported by the connected MONU Server.

Truth Rule:

An operation is not marked completed until a real backend
or verified local processing engine reports completion.
