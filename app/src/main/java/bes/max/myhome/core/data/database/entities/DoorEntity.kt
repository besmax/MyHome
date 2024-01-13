package bes.max.myhome.core.data.database.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DoorEntity : RealmObject {
    @PrimaryKey
    var id: Int = -1
    var name: String = ""
    var snapshot: String? = null
    var room: String? = ""
    var favorites: Boolean = false
}