package bes.max.myhome.doors.data

import bes.max.myhome.core.data.network.NetworkClient
import bes.max.myhome.doors.domain.DoorsRepository
import bes.max.myhome.doors.domain.models.Door
import kotlinx.coroutines.flow.Flow

class DoorsRepositoryImpl(
    private val networkClient: NetworkClient
) : DoorsRepository {
    override suspend fun getFromNetwork(): Flow<Result<List<Door>>> {
        TODO("Not yet implemented")
    }
}