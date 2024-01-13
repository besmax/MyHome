package bes.max.myhome.doors.domain.models

data class Door(
    val name: String,
    val snapshot: String?,
    val room: String?,
    val id: Int,
    val favorites: Boolean,
)
