package io.github.bagdad1970.pedometer.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName="step_days",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
class StepDay {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "date")
    var date: LocalDate = LocalDate.now()

    @ColumnInfo(name = "user_id")
    var userId: Long = 0L

    @ColumnInfo(name = "steps")
    var steps: Int = 0

    constructor()

    constructor(date: LocalDate, userId: Long, steps: Int) : this() {
        this.date = date
        this.userId = userId
        this.steps = steps
    }

}