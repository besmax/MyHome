package bes.max.myhome.di

import bes.max.myhome.core.data.database.dao.CamerasDao
import bes.max.myhome.core.data.database.dao.CamerasDaoImpl
import bes.max.myhome.core.data.database.dao.DoorsDao
import bes.max.myhome.core.data.database.dao.DoorsDaoImpl
import bes.max.myhome.core.data.database.entities.CameraEntity
import bes.max.myhome.core.data.database.entities.DoorEntity
import bes.max.myhome.core.data.network.KtorNetworkClient
import bes.max.myhome.core.data.network.NetworkClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {

    single<HttpClient> {
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    singleOf(::KtorNetworkClient) bind NetworkClient::class

    single {
        val config = RealmConfiguration.create(
            schema = setOf(CameraEntity::class, DoorEntity::class)
        )
        Realm.open(config)
    }

    singleOf(::DoorsDaoImpl) bind DoorsDao::class

    singleOf(::CamerasDaoImpl) bind CamerasDao::class

}