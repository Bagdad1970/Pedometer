package io.github.bagdad1970.pedometer


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class TodayStatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold (
                modifier = Modifier
                    .fillMaxSize(),
                bottomBar = { BottomNavigationBar(AppActivity.TODAY_STATS) }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .navigationBarsPadding()
                ) {
                    TodayStatisticsScreen(modifier = Modifier.padding(innerPadding))
                }

            }
        }
    }

}

@Composable
fun TodayStatisticsScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepsProgressIndicator(
            currentSteps = 7543,
            targetSteps = 10000,
            modifier = Modifier.padding(16.dp)
        )
        TodayInfo()
    }
}

@Composable
fun StepsProgressIndicator(
    currentSteps: Int,
    targetSteps: Int,
    modifier: Modifier = Modifier
) {
    val progress = currentSteps.toFloat() / targetSteps.toFloat()
    val progressPercent = (progress * 100).toInt()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(350.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 14.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$currentSteps",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Цель: $targetSteps",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "$progressPercent%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TodayInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TodayInfoColumn(R.drawable.distance_logo, "1,96", "км")
        TodayInfoColumn(R.drawable.calories_logo, "189", "ккал")
        TodayInfoColumn(R.drawable.time_logo, "1:45", "Время")
    }
}

@Composable
fun TodayInfoColumn(
    iconResId: Int,
    value: String,
    type: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .width(60.dp)
            .height(120.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = type,
            modifier = Modifier.size(30.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = type,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}