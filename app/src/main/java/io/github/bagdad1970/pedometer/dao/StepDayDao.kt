package io.github.bagdad1970.pedometer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.bagdad1970.pedometer.entity.StepDay
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface StepDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stepDay: StepDay): Long

    @Query("SELECT * FROM step_days WHERE user_id = :userId AND date = :date")
    suspend fun getByUserAndDate(userId: Long, date: LocalDate): StepDay?

    @Query("SELECT steps FROM step_days WHERE user_id = :userId ORDER BY date DESC LIMIT 7")
    suspend fun getForWeek(userId: Long): List<Int>

    @Query("SELECT MAX(steps) FROM step_days WHERE user_id = :userId")
    suspend fun getMaxStepsPerDay(userId: Long): Int

}