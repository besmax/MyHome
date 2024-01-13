package bes.max.myhome.core.data.database.dao

import bes.max.myhome.core.data.database.entities.CameraEntity
import kotlinx.coroutines.flow.Flow

interface CamerasDao {
    fun getAllCameras(): List<CameraEntity>

    fun deleteAllCameras()

    fun addAllCameras(cameras: List<CameraEntity>)

    fun updateCamera(camera: CameraEntity)
}