package io.github.bagdad1970.pedometer.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.bagdad1970.pedometer.settings.Sex

@Entity(tableName="users")
class User {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var username: String
    var email: String
    var password: String
    var age: Int = 25
    var language: String = "EN"
    var sex: Sex = Sex.MALE
    var weight: Int = 80
    var height: Int = 180
    var stepLength: Float = 0.734F
    var target: Int = 10000

    constructor(username: String, email: String, password: String) {
        this.username = username
        this.email = email
        this.password = password
    }

}