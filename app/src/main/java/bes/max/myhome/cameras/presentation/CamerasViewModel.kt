package bes.max.myhome.cameras.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.myhome.cameras.domain.CamerasRepository
import bes.max.myhome.cameras.domain.models.Camera
import bes.max.myhome.cameras.domain.usecases.GetCamerasUseCase
import bes.max.myhome.core.domain.models.ErrorType
import bes.max.myhome.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CamerasViewModel(
    private val getCamerasUseCase: GetCamerasUseCase,
    private val repository: CamerasRepository
) : ViewModel() {

    private val _screenState = MutableLiveData<CamerasScreenState>()
    val screenState: LiveData<CamerasScreenState> = _screenState

    init {
        getCameras()
    }

    fun getCameras() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.value = CamerasScreenState.Loading
            getCamerasUseCase.execute().collect() { response ->
                when (response) {
                    is Resource.Success -> {
                        if (response.data.isNullOrEmpty()) {
                            _screenState.postValue(
                                CamerasScreenState.Error(ErrorType.NO_CONTENT)
                            )
                        } else {
                            _screenState.postValue(
                                CamerasScreenState.Content(response.data)
                            )
                        }
                    }

                    is Resource.Error -> {
                        _screenState.postValue(
                            CamerasScreenState.Error(response.errorType ?: ErrorType.SERVER_ERROR)
                        )
                    }
                }
            }
        }
    }

    fun updateCamera(camera: Camera) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCameraInDb(camera)
        }
    }
}