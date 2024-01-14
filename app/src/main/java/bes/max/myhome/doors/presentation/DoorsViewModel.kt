package bes.max.myhome.doors.presentation

import androidx.lifecycle.ViewModel
import bes.max.myhome.doors.domain.DoorsRepository

class DoorsViewModel(
    private val repository: DoorsRepository
) : ViewModel() {
}