package io.github.bagdad1970.pedometer

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import io.github.bagdad1970.pedometer.dao.StepDayDao
import io.github.bagdad1970.pedometer.entity.StepDay
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import androidx.core.content.edit

class StepCounterManager(
    private val context: Context,
    private val stepDayDao: StepDayDao
) {

    companion object {
        private const val TAG = "StepCounterManager"
        private const val PREFS_NAME = "step_counter_prefs"
        private const val KEY_STEPS_SINCE_PAUSE = "steps_since_pause"
        private const val KEY_LAST_SAVED_STEPS = "last_saved_steps"
        private const val KEY_LAST_SAVE_TIME = "last_save_time"
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var sensorManager: SensorManager
    private var stepDetector: Sensor? = null
    private var sensorEventListener: SensorEventListener? = null
    private var currentStepsInMemory = 0

    private val _currentSteps = MutableStateFlow(0)

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    suspend fun startTracking(userId: Long) {
        Log.d(TAG, "Starting step tracking for user: $userId")

        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepDetector == null) return

        val savedSteps = loadSavedSteps(userId)
        currentStepsInMemory = savedSteps
        _currentSteps.value = savedSteps

        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                        handleStepDetected(userId)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }
        }

        sensorManager.registerListener(
            sensorEventListener,
            stepDetector,
            SensorManager.SENSOR_DELAY_FASTEST
        )

        Log.d(TAG, "Step detector registered")

        startPeriodicSaving(userId)
    }

    private fun handleStepDetected(userId: Long) {
        currentStepsInMemory++
        _currentSteps.value = currentStepsInMemory

        saveStepTemporarily()

        if (currentStepsInMemory % 10 == 0) {
            scope.launch {
                saveStepsToDatabase(userId, currentStepsInMemory)
            }
        }
    }

    private fun saveStepTemporarily() {
        prefs.edit {
            putInt(KEY_STEPS_SINCE_PAUSE, currentStepsInMemory)
                .putLong(KEY_LAST_SAVE_TIME, System.currentTimeMillis())
        }
    }

    private suspend fun saveStepsToDatabase(userId: Long, steps: Int) {
        val today = LocalDate.now()
        var stepDay = stepDayDao.getByUserAndDate(userId, today)

        if (stepDay == null) {
            stepDay = StepDay(
                date = today,
                userId = userId,
                steps = steps
            )
            stepDay.id = stepDayDao.insert(stepDay)
        }
        else {
            stepDay.steps = steps
            stepDayDao.insert(stepDay)
        }

        prefs.edit {
            putInt(KEY_LAST_SAVED_STEPS, steps)
        }
    }

    private suspend fun loadSavedSteps(userId: Long): Int {
        val today = LocalDate.now()
        val stepDay = stepDayDao.getByUserAndDate(userId, today)

        return stepDay?.steps ?: 0
    }

    private fun startPeriodicSaving(userId: Long) {
        scope.launch {
            while (isActive) {
                delay(30000)

                if (currentStepsInMemory > 0) {
                    saveStepsToDatabase(userId, currentStepsInMemory)
                }
            }
        }
    }

    suspend fun stopTracking(userId: Long) {
        if (currentStepsInMemory > 0) {
            saveStepsToDatabase(userId, currentStepsInMemory)
        }

        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
        }
        sensorEventListener = null

        prefs.edit {
            remove(KEY_STEPS_SINCE_PAUSE)
        }
    }

    suspend fun getTodaySteps(userId: Long): Int {
        if (currentStepsInMemory > 0) {
            return currentStepsInMemory
        }

        return loadSavedSteps(userId)
    }

    suspend fun resumeTracking(userId: Long) {
        val lastSteps = prefs.getInt(KEY_LAST_SAVED_STEPS, 0)
        val stepsSincePause = prefs.getInt(KEY_STEPS_SINCE_PAUSE, 0)

        currentStepsInMemory = maxOf(lastSteps, stepsSincePause)
        _currentSteps.value = currentStepsInMemory

        startTracking(userId)
    }

    fun cleanup() {
        scope.cancel()
    }
}