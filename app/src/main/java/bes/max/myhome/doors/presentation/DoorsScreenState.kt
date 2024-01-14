package bes.max.myhome.doors.presentation

import bes.max.myhome.core.domain.models.ErrorType
import bes.max.myhome.doors.domain.models.Door

sealed interface DoorsScreenState {
    object Loading : DoorsScreenState

    data class Content(val doors: List<Door>) : DoorsScreenState

    data class Error(val error: ErrorType) : DoorsScreenState
}