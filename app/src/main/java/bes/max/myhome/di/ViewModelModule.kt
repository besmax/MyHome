package bes.max.myhome.di

import bes.max.myhome.cameras.presentation.CamerasViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CamerasViewModel)
}