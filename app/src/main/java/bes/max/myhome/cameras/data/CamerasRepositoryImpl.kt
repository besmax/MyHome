package bes.max.myhome.cameras.data

import bes.max.myhome.cameras.data.dto.AllCamerasRequest
import bes.max.myhome.cameras.data.dto.AllCamerasResponse
import bes.max.myhome.cameras.domain.CamerasRepository
import bes.max.myhome.cameras.domain.models.Camera
import bes.max.myhome.core.data.database.dao.CamerasDao
import bes.max.myhome.core.data.network.KtorNetworkClient
import bes.max.myhome.core.data.network.NetworkClient
import bes.max.myhome.core.domain.models.ErrorType
import bes.max.myhome.util.Resource
import bes.max.myhome.util.map
import bes.max.myhome.util.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CamerasRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dao: CamerasDao
) : CamerasRepository {
    override suspend fun getFromNetwork(): Flow<Resource<List<Camera>>> = flow {
        val response = networkClient.doRequest(AllCamerasRequest)
        when (response.resultCode) {
            KtorNetworkClient.CODE_NO_INTERNET -> emit(Resource.Error(ErrorType.NO_INTERNET))
            KtorNetworkClient.CODE_SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            KtorNetworkClient.CODE_SUCCESS -> emit(
                Resource.Success(
                    data = (response as AllCamerasResponse).mapToList()
                        .map { entity -> entity.map() }
                )
            )
        }

    }

    override suspend fun insertListToDb(cameras: List<Camera>) {
        val entities = cameras.map { model -> model.map() }
        dao.addAllCameras(entities)
    }

    override suspend fun getFromDb(): Flow<List<Camera>> =
        dao.getAllCamerasAsFlow().map { entities -> entities.map { it.map() } }

    override suspend fun updateCameraInDb(camera: Camera) {
        dao.updateCamera(camera.map())
    }
}
