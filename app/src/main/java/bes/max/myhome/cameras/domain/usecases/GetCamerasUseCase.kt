package bes.max.myhome.cameras.domain.usecases

import bes.max.myhome.cameras.domain.CamerasRepository
import bes.max.myhome.cameras.domain.models.Camera
import bes.max.myhome.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCamerasUseCase(
    private val repository: CamerasRepository
) {

    suspend fun execute(): Flow<Resource<List<Camera>>> = flow {
        val camerasFromDb = repository.getFromDb()

        if (camerasFromDb.isEmpty()) {
            repository.getFromNetwork().collect() { response ->
                when (response) {
                    is Resource.Success -> {
                        if (response.data != null) {
                            repository.insertListToDb(response.data)
                            emit(
                                Resource.Success(data = repository.getFromDb())
                            )
                        } else {
                            emit(Resource.Success(data = emptyList()))
                        }
                    }

                    is Resource.Error -> emit(response)
                }
            }

        } else {
            emit(Resource.Success(data = camerasFromDb))
        }
    }

}