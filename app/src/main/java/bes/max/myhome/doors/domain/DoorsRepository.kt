package bes.max.myhome.doors.domain

import bes.max.myhome.doors.domain.models.Door
import kotlinx.coroutines.flow.Flow

interface DoorsRepository {
    suspend fun getFromNetwork(): Flow<Result<List<Door>>>

}