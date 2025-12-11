package io.github.bagdad1970.pedometer

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.bagdad1970.pedometer.dao.UserDao
import io.github.bagdad1970.pedometer.entity.User
import io.github.bagdad1970.pedometer.entity.StepDay
import androidx.room.Room
import androidx.room.TypeConverters
import io.github.bagdad1970.pedometer.dao.StepDayDao
import io.github.bagdad1970.pedometer.entity.SexConverter
import io.github.bagdad1970.pedometer.entity.LocalDateConverter

@Database(entities = [(User::class), (StepDay::class)], version = 1)
@TypeConverters(SexConverter::class, LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun stepDayDao(): StepDayDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "pedometer.db"

                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}