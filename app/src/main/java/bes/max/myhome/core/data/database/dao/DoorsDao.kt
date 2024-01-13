package bes.max.myhome.core.data.database.dao

import bes.max.myhome.core.data.database.entities.DoorEntity
import kotlinx.coroutines.flow.Flow

interface DoorsDao {
    fun getAllDoorsAsFlow(): Flow<List<DoorEntity>>

    fun deleteAllDoors()

    fun addAllDoors(doors: List<DoorEntity>)

    fun updateDoor(door: DoorEntity)
}