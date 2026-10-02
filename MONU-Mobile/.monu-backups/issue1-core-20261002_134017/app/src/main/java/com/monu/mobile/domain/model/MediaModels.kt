package com.monu.mobile.domain.model

enum class MONUMediaType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    ARCHIVE,
    OTHER
}

enum class MONUMediaOperation {
    GENERATE_IMAGE,
    EDIT_IMAGE,
    ENHANCE_IMAGE,
    RESIZE_IMAGE,
    CROP_IMAGE,
    REMOVE_BACKGROUND,
    CONVERT_IMAGE,

    GENERATE_VIDEO,
    IMAGE_TO_VIDEO,
    VIDEO_TO_IMAGE,
    TRIM_VIDEO,
    CUT_VIDEO,
    MERGE_VIDEO,
    EXTRACT_FRAMES,
    COMPRESS_VIDEO,
    CONVERT_VIDEO,

    EXTRACT_AUDIO,
    CONVERT_AUDIO,
    PROCESS_VOICE,
    GENERATE_SPEECH,

    ANALYZE_MEDIA,
    CUSTOM
}

enum class MONUMediaJobStatus {
    DRAFT,
    READY,
    QUEUED,
    UPLOADING,
    STARTING,
    RUNNING,
    PROCESSING,
    VERIFYING,
    COMPLETED,
    FAILED,
    CANCELLED,
    UNKNOWN
}

data class MONUMediaAsset(
    val id: String,
    val uri: String,
    val name: String,
    val type: MONUMediaType,
    val mimeType: String? = null,
    val sizeBytes: Long? = null
)

data class MONUMediaJob(
    val id: String,
    val operation: MONUMediaOperation,
    val status: MONUMediaJobStatus,
    val inputAssets: List<MONUMediaAsset> = emptyList(),
    val progress: Int? = null,
    val currentStage: String? = null,
    val resultUri: String? = null,
    val error: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
