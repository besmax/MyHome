package bes.max.myhome.cameras.data.dto

import bes.max.myhome.core.data.dto.responses.Response
import kotlinx.serialization.Serializable

@Serializable
data class AllCamerasResponse(
    val success: Boolean,
    val data: DataDto,
) : Response()

@Serializable
data class DataDto(
    val room: List<String>,
    val cameras: List<CameraDto>
)

@Serializable
data class CameraDto(
    val name: String,
    val snapshot: String,
    val room: String?,
    val id: Int,
    val favorites: Boolean,
    val rec: Boolean
)


