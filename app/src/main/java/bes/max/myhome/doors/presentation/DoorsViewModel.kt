package bes.max.myhome.doors.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.myhome.core.domain.models.ErrorType
import bes.max.myhome.doors.domain.DoorsRepository
import bes.max.myhome.doors.domain.GetDoorsUseCase
import bes.max.myhome.doors.domain.models.Door
import bes.max.myhome.util.Resource
import kotlinx.coroutines.launch

class DoorsViewModel(
    private val repository: DoorsRepository,
    private val getDoorsUseCase: GetDoorsUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<DoorsScreenState>()
    val screenState: LiveData<DoorsScreenState> = _screenState

    init {
        getDoors()
    }

    fun getDoors() {
        viewModelScope.launch {
            _screenState.value = DoorsScreenState.Loading
            getDoorsUseCase.execute().collect() { response ->
                when (response) {
                    is Resource.Success -> {
                        if (response.data.isNullOrEmpty()) {
                            _screenState.postValue(
                                DoorsScreenState.Error(ErrorType.NO_CONTENT)
                            )
                        } else {
                            _screenState.postValue(
                                DoorsScreenState.Content(response.data)
                            )
                        }
                    }

                    is Resource.Error -> {
                        _screenState.postValue(
                            DoorsScreenState.Error(response.errorType ?: ErrorType.SERVER_ERROR)
                        )
                    }
                }
            }
        }
    }

    fun updateDoor(door: Door) {
        viewModelScope.launch {
            repository.updateDoorInDb(door)
        }
    }
}