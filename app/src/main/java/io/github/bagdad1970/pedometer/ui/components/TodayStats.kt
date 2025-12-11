package io.github.bagdad1970.pedometer.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.dao.UserDao
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun TodayStats(
    todaySteps: Int,
    targetSteps: Int,
    distance: String,
    calories: String,
    time: String,
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
                distance = distance,
                calories = calories,
                time = time,
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
    distance: String,
    calories: String,
    time: String,
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
        TodayInfo(
            distance,
            calories,
            time
        )
    }
}


@Composable
fun TodayInfo(
    distance: String,
    calories: String,
    time: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.today_info)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TodayInfoColumn(R.drawable.distance_logo, distance, stringResource(id = R.string.kilometers_today))
        TodayInfoColumn(R.drawable.calories_logo, calories, stringResource(id = R.string.kilocalories_today))
        TodayInfoColumn(R.drawable.time_logo, time, stringResource(id = R.string.time_today))
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
            .padding(dimensionResource(id = R.dimen.today_info_column_padding))
            .width(dimensionResource(id = R.dimen.today_info_column_width))
            .height(dimensionResource(id = R.dimen.today_info_column_height))
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = type,
            modifier = Modifier.size(dimensionResource(id = R.dimen.today_info_icon))
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.today_info_spacer1)))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.today_info_spacer2)))

        Text(
            text = type,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

