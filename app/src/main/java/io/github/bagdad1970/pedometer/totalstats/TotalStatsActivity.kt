package io.github.bagdad1970.pedometer.totalstats


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
import androidx.compose.ui.unit.dp
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.ui.components.TotalInfo
import io.github.bagdad1970.pedometer.ui.components.WeekInfo


class TotalStatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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

@Composable
fun TotalStatsScreen(modifier: Modifier) {

    Column(
        modifier = modifier
            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {

        WeekInfo()

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TotalInfo(
                modifier = Modifier.weight(1f),
                title = "Максимум шагов за день",
                value = "25648"
            )

            Spacer(modifier = Modifier.width(8.dp))

            TotalInfo(
                modifier = Modifier.weight(1f),
                title = "Максимальный путь за день",
                value = "9.3 км"
            )
        }
    }
}