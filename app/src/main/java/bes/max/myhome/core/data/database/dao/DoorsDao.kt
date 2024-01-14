package bes.max.myhome.core.data.database.dao

import bes.max.myhome.core.data.database.entities.DoorEntity

interface DoorsDao {
    fun getAllDoors(): List<DoorEntity>

    fun deleteAllDoors()

    fun addAllDoors(doors: List<DoorEntity>)

    fun updateDoor(door: DoorEntity)
}