package bes.max.myhome.doors.data

import bes.max.myhome.core.data.database.dao.DoorsDao
import bes.max.myhome.core.data.network.KtorNetworkClient
import bes.max.myhome.core.data.network.NetworkClient
import bes.max.myhome.core.domain.models.ErrorType
import bes.max.myhome.doors.data.dto.AllDoorsRequest
import bes.max.myhome.doors.data.dto.AllDoorsResponse
import bes.max.myhome.doors.domain.DoorsRepository
import bes.max.myhome.doors.domain.models.Door
import bes.max.myhome.util.Resource
import bes.max.myhome.util.map
import bes.max.myhome.util.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DoorsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dao: DoorsDao
) : DoorsRepository {
    override suspend fun getFromNetwork(): Flow<Resource<List<Door>>> = flow {
        val response = networkClient.doRequest(AllDoorsRequest)
        when (response.resultCode) {
            KtorNetworkClient.CODE_NO_INTERNET -> emit(Resource.Error(ErrorType.NO_INTERNET))
            KtorNetworkClient.CODE_SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            KtorNetworkClient.CODE_SUCCESS -> emit(
                Resource.Success(
                    data = (response as AllDoorsResponse).mapToList()
                        .map { entity -> entity.map() }
                )
            )
        }
    }

    override suspend fun insertListToDb(doors: List<Door>) {
        val entities = doors.map { model -> model.map() }
        dao.addAllDoors(entities)
    }

    override suspend fun getFromDb(): Flow<List<Door>> =
        dao.getAllDoorsAsFlow().map { entities -> entities.map { it.map() } }

    override suspend fun updateDoorInDb(door: Door) {
        dao.updateDoor(door.map())
    }
}