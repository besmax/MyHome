package bes.max.myhome.doors.domain

import bes.max.myhome.doors.domain.models.Door
import bes.max.myhome.util.Resource
import kotlinx.coroutines.flow.Flow

interface DoorsRepository {
    suspend fun getFromNetwork(): Flow<Resource<List<Door>>>

}