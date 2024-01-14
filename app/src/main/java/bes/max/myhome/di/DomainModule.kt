package bes.max.myhome.di

import bes.max.myhome.cameras.data.CamerasRepositoryImpl
import bes.max.myhome.cameras.domain.CamerasRepository
import bes.max.myhome.cameras.domain.usecases.GetCamerasUseCase
import bes.max.myhome.doors.data.DoorsRepositoryImpl
import bes.max.myhome.doors.domain.DoorsRepository
import bes.max.myhome.doors.domain.GetDoorsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {

    singleOf(::CamerasRepositoryImpl) bind CamerasRepository::class

    singleOf(::DoorsRepositoryImpl) bind DoorsRepository::class

    factoryOf(::GetCamerasUseCase)

    factoryOf(::GetDoorsUseCase)
}