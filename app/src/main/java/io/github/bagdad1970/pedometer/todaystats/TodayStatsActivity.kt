package io.github.bagdad1970.pedometer.todaystats

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.ui.components.StepsProgress
import io.github.bagdad1970.pedometer.ui.components.TodayInfo
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import io.github.bagdad1970.pedometer.utils.LocaleHelper
import kotlinx.coroutines.delay

class TodayStatsActivity : ComponentActivity() {

    companion object {
        var hasShownSplash = false
        const val EXTRA_TODAY_STEPS = "today_steps"
        const val EXTRA_TARGET_STEPS = "target_steps"
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.setLocale(it, LocaleHelper.getLanguage(it)) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            PedometerTheme {
                var isLoading by remember { mutableStateOf(!hasShownSplash) }
                var todaySteps by remember { mutableIntStateOf(8452) }
                var targetSteps by remember { mutableIntStateOf(10000) }

                LaunchedEffect(Unit) {
                    if (!hasShownSplash) {
                        delay(500)
                        hasShownSplash = true
                        isLoading = false
                    }
                }

                if (isLoading)
                    SplashScreen()
                else
                    TodayStats(
                        todaySteps = todaySteps,
                        targetSteps = targetSteps,
                        onStepsChanged = { todaySteps = it }
                    )
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pedometer",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun TodayStats(
    todaySteps: Int,
    targetSteps: Int,
    onStepsChanged: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentScreen = AppActivity.TODAY_STATS,
                todaySteps = todaySteps,
                targetSteps = targetSteps
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
        ) {
            TodayStatisticsScreen(
                modifier = Modifier.padding(innerPadding),
                todaySteps = todaySteps,
                targetSteps = targetSteps,
                onStepsChanged = onStepsChanged
            )
        }
    }
}

@Composable
fun TodayStatisticsScreen(
    modifier: Modifier,
    todaySteps: Int,
    targetSteps: Int,
    onStepsChanged: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.today_screen)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepsProgress(
            currentSteps = todaySteps,
            targetSteps = targetSteps,
            onResetSteps = { onStepsChanged(0) },
            modifier = Modifier.padding(dimensionResource(id = R.dimen.steps_progress))
        )
        TodayInfo()
    }
}