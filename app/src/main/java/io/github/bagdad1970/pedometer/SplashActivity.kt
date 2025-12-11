package io.github.bagdad1970.pedometer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import io.github.bagdad1970.pedometer.todaystats.TodayStatsActivity
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PedometerTheme {
                SplashScreenContent(onFinished = {
                    startActivity(Intent(this@SplashActivity, TodayStatsActivity::class.java))
                    finish()
                })
            }
        }
    }
}

@Composable
fun SplashScreenContent(onFinished: () -> Unit) {
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
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp)
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(800)
        onFinished()
    }
}