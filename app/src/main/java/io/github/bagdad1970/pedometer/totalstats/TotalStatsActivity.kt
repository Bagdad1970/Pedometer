package io.github.bagdad1970.pedometer.totalstats

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.AppDatabase
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.PedometerApplication
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.dao.StepDayDao
import io.github.bagdad1970.pedometer.dao.UserDao
import io.github.bagdad1970.pedometer.entity.StepDay
import io.github.bagdad1970.pedometer.ui.components.TotalInfo
import io.github.bagdad1970.pedometer.ui.components.WeekInfo
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import io.github.bagdad1970.pedometer.utils.LocaleHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TotalStatsActivity : ComponentActivity() {

    private lateinit var db: AppDatabase
    private lateinit var stepDayDao: StepDayDao
    private lateinit var userDao: UserDao

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.setLocale(it, LocaleHelper.getLanguage(it)) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as PedometerApplication
        db = app.database
        stepDayDao = db.stepDayDao()
        userDao = db.userDao()

        setContent {
            PedometerTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            currentScreen = AppActivity.TOTAL_STATS,
                            todaySteps = 0,
                            targetSteps = 10000
                        )
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TotalStatsScreenContent(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun TotalStatsScreenContent(modifier: Modifier) {
        var maxSteps by remember { mutableIntStateOf(0) }
        var maxDistance by remember { mutableStateOf("0.0") }
        var stepLength by remember { mutableFloatStateOf(0.75f) }
        var stepsByDayPerWeek by remember { mutableStateOf(listOf(0, 0, 0, 0, 0, 0, 0)) }
        var weekDaysInfo by remember { mutableStateOf(emptyList<Pair<String, Int>>()) }
        var userId by remember { mutableLongStateOf(-1L) }

        LaunchedEffect(Unit) {
            lifecycleScope.launch {
                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                val email = prefs.getString("user_email", "") ?: ""

                if (email.isNotEmpty()) {
                    val user = withContext(Dispatchers.IO) {
                        db.userDao().findByEmail(email)
                    }
                    if (user.id != 0L) {
                        userId = user.id
                        stepLength = user.stepLength

                        val maxStepsValue = withContext(Dispatchers.IO) {
                            stepDayDao.getMaxStepsPerDay(userId)
                        }

                        maxSteps = maxStepsValue ?: 0
                        if (maxSteps > 0) {
                            val distance = (maxSteps * stepLength) / 1000.0
                            maxDistance = String.format("%.2f", distance)
                        }

                        val weeklyData = withContext(Dispatchers.IO) {
                            getStepsWithWeekDays(userId)
                        }
                        weekDaysInfo = weeklyData

                        stepsByDayPerWeek = weeklyData.map { it.second }
                    }
                }
            }
        }

        TotalStatsScreen(
            modifier = modifier,
            maxSteps = maxSteps.toString(),
            maxDistance = maxDistance,
            stepsByDayPerWeek = stepsByDayPerWeek,
            weekDaysInfo = weekDaysInfo
        )
    }

    private suspend fun getStepsWithWeekDays(userId: Long): List<Pair<String, Int>> {
        return withContext(Dispatchers.IO) {
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("EEE")
            val result = mutableListOf<Pair<String, Int>>()

            for (i in 0..6) {
                val date = today.minusDays(i.toLong())
                val dayName = date.format(formatter)
                val steps = stepDayDao.getByUserAndDate(userId, date)?.steps ?: 0
                result.add(Pair(dayName, steps))
            }

            result.reversed()
        }
    }

}

@Composable
fun TotalStatsScreen(
    modifier: Modifier,
    maxSteps: String,
    maxDistance: String,
    stepsByDayPerWeek: List<Int>,
    weekDaysInfo: List<Pair<String, Int>> = emptyList()
) {
    Column(
        modifier = modifier
            .padding(
                top = dimensionResource(id = R.dimen.total_screen_top),
                start = dimensionResource(id = R.dimen.total_screen_start),
                end = dimensionResource(id = R.dimen.total_screen_end),
                bottom = dimensionResource(id = R.dimen.total_screen_bottom)
            )
    ) {
        WeekInfo(stepsByDayPerWeek = stepsByDayPerWeek)

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TotalInfo(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.max_steps_per_day),
                value = maxSteps
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.total_screen_spacer)))

            TotalInfo(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.max_distance_per_day),
                value = "$maxDistance ${stringResource(id = R.string.total_max_distance_metric)}"
            )
        }
    }
}