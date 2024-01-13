package bes.max.myhome.cameras.domain

import bes.max.myhome.cameras.domain.models.Camera
import bes.max.myhome.util.Resource
import kotlinx.coroutines.flow.Flow

interface CamerasRepository {
    suspend fun getFromNetwork(): Flow<Resource<List<Camera>>>
}