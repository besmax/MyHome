package bes.max.myhome.doors.domain

import bes.max.myhome.doors.domain.models.Door
import bes.max.myhome.util.Resource
import kotlinx.coroutines.flow.Flow

interface DoorsRepository {
    suspend fun getFromNetwork(): Flow<Resource<List<Door>>>

    suspend fun insertListToDb(doors: List<Door>)

    suspend fun getFromDb(): Flow<List<Door>>

    suspend fun updateDoorInDb(door: Door)
}