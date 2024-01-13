package bes.max.myhome.app

import android.app.Application
import bes.max.myhome.di.dataModule
import bes.max.myhome.di.domainModule
import bes.max.myhome.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }
    }
}