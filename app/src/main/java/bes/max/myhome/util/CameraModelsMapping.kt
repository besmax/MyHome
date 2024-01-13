package bes.max.myhome.util

import bes.max.myhome.cameras.data.dto.AllCamerasResponse
import bes.max.myhome.cameras.domain.models.Camera
import bes.max.myhome.core.data.database.entities.CameraEntity

fun AllCamerasResponse.mapToList(): List<CameraEntity> {
    return data.cameras.map { dto ->
        CameraEntity().apply {
            id = dto.id
            name = dto.name
            snapshot = dto.snapshot
            room = dto.room
            favorites = dto.favorites
            rec = dto.rec
        }
    }
}

fun CameraEntity.map() = Camera(
    name = name,
    snapshot = snapshot,
    room = room,
    id = id,
    favorites = favorites,
    rec = rec
)

fun Camera.map() = CameraEntity().apply {
    name = this@map.name
    snapshot = this@map.snapshot
    room = this@map.room
    id = this@map.id
    favorites = this@map.favorites
    rec = this@map.rec
}


