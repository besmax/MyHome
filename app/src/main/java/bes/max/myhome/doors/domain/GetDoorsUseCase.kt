package bes.max.myhome.doors.domain

import bes.max.myhome.core.domain.models.ErrorType
import bes.max.myhome.doors.domain.models.Door
import bes.max.myhome.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDoorsUseCase(
    private val repository: DoorsRepository
) {

    fun execute(): Flow<Resource<List<Door>>> = flow {
        val doorsFromDb = repository.getFromDb()

        if (doorsFromDb.isEmpty()) {
            repository.getFromNetwork().collect() { response ->
                when (response) {
                    is Resource.Success -> {
                        if (response.data != null) {
                            repository.insertListToDb(response.data)
                            emit(Resource.Success(data = repository.getFromDb()))
                        } else {
                            emit(Resource.Error(errorType = ErrorType.NO_CONTENT))
                        }
                    }

                    is Resource.Error -> emit(response)
                }
            }
        } else {
            emit(Resource.Success(data = doorsFromDb))
        }
    }
}