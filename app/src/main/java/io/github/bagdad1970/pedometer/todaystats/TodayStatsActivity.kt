package io.github.bagdad1970.pedometer.todaystats

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import io.github.bagdad1970.pedometer.AppDatabase
import io.github.bagdad1970.pedometer.PedometerApplication
import io.github.bagdad1970.pedometer.StepCounterManager
import io.github.bagdad1970.pedometer.entity.User
import io.github.bagdad1970.pedometer.settings.Sex
import io.github.bagdad1970.pedometer.ui.components.TodayStats
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import io.github.bagdad1970.pedometer.utils.Calculations
import kotlinx.coroutines.launch

class TodayStatsActivity : ComponentActivity() {

    private lateinit var db: AppDatabase
    private lateinit var stepCounterManager: StepCounterManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as PedometerApplication
        db = app.database

        stepCounterManager = StepCounterManager(
            context = this,
            stepDayDao = db.stepDayDao()
        )

        setContent {
            PedometerTheme {
                PermissionAndContent()
            }
        }
    }

    @Composable
    private fun PermissionAndContent() {
        val context = LocalContext.current
        var hasPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            hasPermission = isGranted
        }

        LaunchedEffect(Unit) {
            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }

        if (!hasPermission) {
            PermissionScreen {
                permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }
        else {
            MainContent()
        }
    }

    @Composable
    private fun PermissionScreen(onGrantPermission: () -> Unit) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Необходимо разрешение",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Для подсчета шагов необходимо разрешение на доступ к датчикам активности",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onGrantPermission,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Разрешить")
                }
            }
        }
    }

    @Composable
    private fun MainContent() {
        var todaySteps by remember { mutableIntStateOf(0) }
        var targetSteps by remember { mutableIntStateOf(10000) }
        var stepLength by remember { mutableFloatStateOf(0.749F) }
        var height by remember { mutableIntStateOf(170) }
        var weight by remember { mutableIntStateOf(65) }
        var age by remember { mutableIntStateOf(25) }
        var sex by remember { mutableStateOf(Sex.MALE) }
        var userId by remember { mutableLongStateOf(-1L) }
        var distance by remember { mutableStateOf("0.00") }
        var calories by remember { mutableStateOf("0") }
        var walkingTime by remember { mutableStateOf("0:00") }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val email = prefs.getString("user_email", "") ?: ""

            if (email.isNotEmpty()) {
                val user = db.userDao().findByEmail(email)
                if (user.id != 0L) {
                    userId = user.id
                    targetSteps = user.target
                    weight = user.weight
                    height = user.height
                    sex = user.sex
                    age = user.age
                    stepLength = user.stepLength

                    stepCounterManager.startTracking(user.id)
                }
            }
        }

        LaunchedEffect(userId) {
            if (userId != -1L) {
                while (true) {
                    val steps = stepCounterManager.getTodaySteps(userId)
                    if (steps != todaySteps) {
                        todaySteps = steps
                    }
                    kotlinx.coroutines.delay(1000)
                }
            }
        }

        LaunchedEffect(todaySteps, height, weight, age, sex, stepLength) {
            distance = String.format("%.2f", Calculations.calculateDistance(todaySteps, stepLength))

            calories = String.format("%.0f", Calculations.calculateKiloCalories(height, weight, age, sex))

            val walkingTimeMinutes = Calculations.calculateWalkingTime(todaySteps, stepLength)
            val hours = walkingTimeMinutes.toInt() / 60
            val minutes = walkingTimeMinutes.toInt() % 60

            walkingTime = if (hours > 0) {
                String.format("%d:%02d", hours, minutes)
            } else { "$minutes мин" }
        }

        DisposableEffect(Unit) {
            onDispose {
                coroutineScope.launch {
                    if (userId != -1L) {
                        stepCounterManager.stopTracking(userId)
                    }
                }
            }
        }

        TodayStats(
            todaySteps = todaySteps,
            targetSteps = targetSteps,
            distance = distance,
            calories = calories,
            time = walkingTime,
            onStepsChanged = { todaySteps = it }
        )
    }

    override fun onPause() {
        super.onPause()

        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val email = prefs.getString("user_email", "") ?: ""

        if (email.isNotEmpty()) {
            lifecycleScope.launch {
                val user = db.userDao().findByEmail(email)
                if (user.id != 0L) {
                    stepCounterManager.stopTracking(user.id)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val email = prefs.getString("user_email", "") ?: ""

        if (email.isNotEmpty()) {
            lifecycleScope.launch {
                val user = db.userDao().findByEmail(email)
                if (user.id != 0L) {
                    stepCounterManager.resumeTracking(user.id)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stepCounterManager.cleanup()
    }
}