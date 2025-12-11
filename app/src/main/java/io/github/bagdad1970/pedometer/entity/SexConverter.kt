package io.github.bagdad1970.pedometer.entity

import androidx.room.TypeConverter
import io.github.bagdad1970.pedometer.settings.Sex

class SexConverter {

    @TypeConverter
    fun fromSexToString(sex: Sex): String {
        return when (sex) {
            Sex.MALE -> "male"
            Sex.FEMALE -> "female"
        }
    }

    @TypeConverter
    fun fromStringToSex(value: String): Sex {
        return when (value) {
            "male" -> Sex.MALE
            "female" -> Sex.FEMALE
            else -> throw IllegalArgumentException("Unknown sex value: $value")
        }
    }

}