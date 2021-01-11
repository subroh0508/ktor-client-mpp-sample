package net.subroh0508.colormaster.api.json

import kotlinx.serialization.Serializable

@Serializable
data class LiveNameJson internal constructor(
    val name: Map<String, String>,
)
