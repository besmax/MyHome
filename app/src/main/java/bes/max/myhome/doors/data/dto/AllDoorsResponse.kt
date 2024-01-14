package bes.max.myhome.doors.data.dto

import bes.max.myhome.core.data.dto.responses.Response
import kotlinx.serialization.Serializable

@Serializable
data class AllDoorsResponse(
    val success: Boolean,
    val data: List<DoorDto>,
) : Response()

@Serializable
data class DoorDto(
    val name: String,
    val snapshot: String? = null,
    val room: String?,
    val id: Int,
    val favorites: Boolean,
)