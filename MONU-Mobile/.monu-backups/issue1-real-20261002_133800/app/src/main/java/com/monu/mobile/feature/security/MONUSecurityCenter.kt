package com.monu.mobile.feature.security

import android.content.Context
import com.monu.mobile.domain.model.MONUSecurityCategory
import com.monu.mobile.domain.model.MONUSecurityFinding
import com.monu.mobile.domain.model.MONUSecurityReport
import com.monu.mobile.domain.model.MONUSecurityStatus

class MONUSecurityCenter(
    private val context: Context
) {

    fun inspect(): MONUSecurityReport {

        val findings = mutableListOf<MONUSecurityFinding>()

        findings += MONUSecurityFinding(
            id = "app_sandbox",
            category = MONUSecurityCategory.APPLICATION,
            status = MONUSecurityStatus.SECURE,
            title = "Android Application Sandbox",
            description = "MONU runs within normal Android application sandbox boundaries.",
            verified = true
        )

        findings += MONUSecurityFinding(
            id = "root_authority",
            category = MONUSecurityCategory.DEVICE,
            status = MONUSecurityStatus.UNKNOWN,
            title = "Root Authority",
            description = "Root authority is not assumed by a normal MONU APK.",
            verified = false
        )

        findings += MONUSecurityFinding(
            id = "kernel_authority",
            category = MONUSecurityCategory.DEVICE,
            status = MONUSecurityStatus.UNKNOWN,
            title = "Kernel Authority",
            description = "Kernel authority is not available to a normal APK unless a legitimate environment explicitly provides it.",
            verified = false
        )

        findings += MONUSecurityFinding(
            id = "private_apps",
            category = MONUSecurityCategory.APPLICATION,
            status = MONUSecurityStatus.SECURE,
            title = "Cross Application Isolation",
            description = "MONU does not assume access to private data belonging to other Android applications.",
            verified = true
        )

        findings += MONUSecurityFinding(
            id = "network",
            category = MONUSecurityCategory.NETWORK,
            status = MONUSecurityStatus.UNKNOWN,
            title = "Network Security",
            description = "Real TLS and server certificate verification requires live connection inspection.",
            verified = false
        )

        return MONUSecurityReport(
            findings = findings
        )
    }
}
