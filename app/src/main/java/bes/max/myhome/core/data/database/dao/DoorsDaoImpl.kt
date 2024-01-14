package bes.max.myhome.core.data.database.dao

import bes.max.myhome.core.data.database.entities.DoorEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class DoorsDaoImpl(
    private val realm: Realm
) : DoorsDao {
    override fun getAllDoors(): List<DoorEntity> {
        return realm.query<DoorEntity>().find()
    }

    override fun deleteAllDoors() {
        realm.writeBlocking {
            delete(query<DoorEntity>())
        }
    }

    override fun addAllDoors(doors: List<DoorEntity>) {
        realm.writeBlocking {
            doors.map { entity -> copyToRealm(entity) }
        }
    }

    override fun updateDoor(door: DoorEntity) {
        realm.writeBlocking {
            val existDoor = query<DoorEntity>("id == ${door.id}").find().first()
            existDoor.let { oldDoor ->
                oldDoor.favorites = door.favorites
                oldDoor.name = door.name
                oldDoor.room = door.room
                oldDoor.snapshot = door.snapshot
            }
        }
    }

}