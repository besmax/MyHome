package bes.max.myhome.core.data.database.dao

import bes.max.myhome.core.data.database.entities.CameraEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CamerasDaoImpl(
    private val realm: Realm
) : CamerasDao {
    override fun getAllCamerasAsFlow(): Flow<List<CameraEntity>> {
        return realm.query<CameraEntity>().asFlow().map { it.list }
    }

    override fun deleteAllCameras() {
        realm.writeBlocking {
            delete(query<CameraEntity>())
        }
    }

    override fun addAllCameras(cameras: List<CameraEntity>) {
        realm.writeBlocking {
            cameras.map { entity -> copyToRealm(entity) }
        }
    }

    override fun updateCamera(camera: CameraEntity) {
        realm.writeBlocking {
            val existCamera = query<CameraEntity>("id == ${camera.id}").find().first()
            existCamera.let { oldCamera ->
                oldCamera.favorites = camera.favorites
                oldCamera.name = camera.name
                oldCamera.rec = camera.rec
                oldCamera.room = camera.room
                oldCamera.snapshot = camera.snapshot
            }
        }
    }
}