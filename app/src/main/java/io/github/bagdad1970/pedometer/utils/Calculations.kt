package io.github.bagdad1970.pedometer.utils

import io.github.bagdad1970.pedometer.settings.Sex

object Calculations {

    var STEP_LENGTH_MALE = 0.413F
    var STEP_LENGTH_FEMALE = 0.415F
    var CALORIES_FEMALE = 161
    var CALORIES_MALE = 5
    var AVERAGE_WALKING_TIME = 4.6F


    fun calculateStepLength(height: Int, sex: Sex) : Float {
        return if (sex == Sex.FEMALE) {
            height * STEP_LENGTH_FEMALE / 100
        } else {
            height * STEP_LENGTH_MALE / 100
        }
    }

    fun calculateKiloCalories(height: Int, weight: Int, age: Int, sex: Sex) : Float {
        val calories = if (sex == Sex.FEMALE) {
            (10 * height) + (6.25F * weight) - (5 * age) - CALORIES_FEMALE
        } else {
            (10 * height) + (6.25F * weight) - (5 * age) + CALORIES_MALE
        }
        return calories / 1000F
    }

    fun calculateDistance(steps: Int, stepLength: Float) : Float {
        return steps * stepLength / 1000F
    }

    fun calculateWalkingTime(steps: Int, stepLength: Float) : Float {
        return calculateDistance(steps, stepLength) / AVERAGE_WALKING_TIME
    }

}