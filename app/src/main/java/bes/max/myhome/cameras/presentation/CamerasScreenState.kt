package bes.max.myhome.cameras.presentation

import bes.max.myhome.cameras.domain.models.Camera
import bes.max.myhome.core.domain.models.ErrorType

sealed interface CamerasScreenState {
    object Loading : CamerasScreenState

    data class Content(val cameras: List<Camera>) : CamerasScreenState

    data class Error(val error: ErrorType) : CamerasScreenState
}