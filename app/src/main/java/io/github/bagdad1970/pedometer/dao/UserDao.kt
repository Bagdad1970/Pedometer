package io.github.bagdad1970.pedometer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.bagdad1970.pedometer.entity.User
import io.github.bagdad1970.pedometer.settings.Sex

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>

    @Query("UPDATE users SET username = :username WHERE id = :id")
    suspend fun updateName(id: Long, username: String)

    @Query("UPDATE users SET language = :language WHERE id = :id")
    suspend fun updateLanguage(id: Long, language: String)

    @Query("UPDATE users SET age = :age WHERE id = :id")
    suspend fun updateAge(id: Long, age: Int)

    @Query("UPDATE users SET weight = :weight WHERE id = :id")
    suspend fun updateWeight(id: Long, weight: Int)

    @Query("UPDATE users SET height = :height WHERE id = :id")
    suspend fun updateHeight(id: Long, height: Int)

    @Query("UPDATE users SET sex = :sex WHERE id = :id")
    suspend fun updateSex(id: Long, sex: Sex)

    @Query("UPDATE users SET target = :target WHERE id = :id")
    suspend fun updateTarget(id: Long, target: Int)

    @Query("UPDATE users SET stepLength = :stepLength WHERE id = :id")
    suspend fun updateStepLength(id: Long, stepLength: Float)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun findByEmail(email: String) : User

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

}