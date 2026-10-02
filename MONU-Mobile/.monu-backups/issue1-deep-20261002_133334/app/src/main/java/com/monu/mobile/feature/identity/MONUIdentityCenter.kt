package com.monu.mobile.feature.identity

import com.monu.mobile.domain.model.MONUSession
import com.monu.mobile.domain.model.MONUSessionStatus
import com.monu.mobile.domain.model.MONUUserIdentity

class MONUIdentityCenter {

    fun currentIdentity(): MONUUserIdentity {
        return MONUUserIdentity(
            id = "unknown",
            displayName = "MONU Owner",
            authenticated = false,
            source = "LOCAL_ARCHITECTURE",
            verified = false
        )
    }

    fun currentSession(): MONUSession {
        return MONUSession(
            id = "unknown",
            status = MONUSessionStatus.UNKNOWN,
            createdAt = 0L,
            expiresAt = null,
            verified = false
        )
    }
}
