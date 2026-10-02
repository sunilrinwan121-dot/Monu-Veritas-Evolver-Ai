# MONU VERIFICATION ENGINE

The Verification Engine determines whether an execution
result has supporting evidence.

Architecture:

EXECUTION CLAIM
↓
VERIFICATION REQUEST
↓
EVIDENCE COLLECTION
↓
EVIDENCE ANALYSIS
↓
VERIFICATION RESULT

Verification results:

- VERIFIED
- REJECTED
- INCONCLUSIVE
- UNKNOWN

Possible evidence:

- Local result
- File existence
- Database record
- Network response
- Server acknowledgement
- User confirmation
- System state

Truth Rule:

EXECUTED
!=
VERIFIED

A successful-looking operation is not automatically
treated as verified.

UNKNOWN or INCONCLUSIVE is preferred over fabricated proof.
