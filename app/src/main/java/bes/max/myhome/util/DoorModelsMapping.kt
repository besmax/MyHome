package bes.max.myhome.util

import bes.max.myhome.core.data.database.entities.DoorEntity
import bes.max.myhome.doors.data.dto.AllDoorsResponse
import bes.max.myhome.doors.domain.models.Door

fun AllDoorsResponse.mapToList(): List<DoorEntity> {
    return data.map { dto ->
        DoorEntity().apply {
            id = dto.id
            name = dto.name
            snapshot = dto.snapshot
            room = dto.room
            favorites = dto.favorites
        }
    }
}

fun DoorEntity.map() = Door(
    name = name,
    snapshot = snapshot,
    room = room,
    id = id,
    favorites = favorites,
)

fun Door.map() = DoorEntity().apply {
    name = this@map.name
    snapshot = this@map.snapshot
    room = this@map.room
    id = this@map.id
    favorites = this@map.favorites
}


