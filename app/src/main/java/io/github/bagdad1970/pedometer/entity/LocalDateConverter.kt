package io.github.bagdad1970.pedometer.entity

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = dateString?.let { LocalDate.parse(it) }
}