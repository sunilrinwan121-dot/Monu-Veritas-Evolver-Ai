package com.monu.mobile.domain.model

enum class AttachmentType {
    IMAGE,
    VIDEO,
    PDF,
    DOCUMENT,
    UNKNOWN
}

data class MONUAttachment(
    val uri: String,
    val name: String,
    val type: AttachmentType,
    val mimeType: String? = null,
    val sizeBytes: Long? = null
)
