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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.ui.components.TotalInfo
import io.github.bagdad1970.pedometer.ui.components.WeekInfo
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import io.github.bagdad1970.pedometer.utils.LocaleHelper


class TotalStatsActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.setLocale(it, LocaleHelper.getLanguage(it)) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            PedometerTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize(),
                    bottomBar = { BottomNavigationBar(AppActivity.TOTAL_STATS) }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TotalStatsScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun TotalStatsScreen(modifier: Modifier) {

    Column(
        modifier = modifier
            .padding(
                top = dimensionResource(id = R.dimen.total_screen_top),
                start = dimensionResource(id = R.dimen.total_screen_start),
                end = dimensionResource(id = R.dimen.total_screen_end),
                bottom = dimensionResource(id = R.dimen.total_screen_bottom)
            )
    ) {
        WeekInfo()

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TotalInfo(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.max_steps_per_day),
                value = "25648"
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.total_screen_spacer)))

            TotalInfo(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.max_distance_per_day),
                value = "9.3 ${stringResource(id = R.string.total_max_distance_metric)}"
            )
        }
    }
}