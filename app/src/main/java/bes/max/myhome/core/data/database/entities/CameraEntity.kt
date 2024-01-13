package bes.max.myhome.core.data.database.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CameraEntity : RealmObject {
    @PrimaryKey
    var id: Int = 1
    var name: String = ""
    var snapshot: String = ""
    var room: String? = null
    var favorites: Boolean = false
    var rec: Boolean = false
}