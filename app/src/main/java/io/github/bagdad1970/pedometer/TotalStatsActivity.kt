package io.github.bagdad1970.pedometer


import java.util.Locale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
    val stepsByDayPerWeek = remember {
        listOf(4609, 5522, 1500, 4980, 10400, 3550, 8907)
    }
    val maxStepsPerDay = stepsByDayPerWeek.maxOrNull() ?: 0
    Column(
        modifier = modifier
            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(7.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Последние 7 дней",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    stepsByDayPerWeek.forEachIndexed { index, steps ->
                        val boxHeight = steps.toFloat() / maxStepsPerDay.toFloat()

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${String.format(Locale.UK, "%.1f", steps / 1000.0)} т.",
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height((boxHeight * 80).dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )

                            Text(
                                text = when (index) {
                                    0 -> "Пн"
                                    1 -> "Вт"
                                    2 -> "Ср"
                                    3 -> "Чт"
                                    4 -> "Пт"
                                    5 -> "Сб"
                                    else -> "Вс"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                    }
                }
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CardInfo(
                modifier = Modifier.weight(1f),
                title = "Максимум шагов за день",
                value = "25648"
            )

            Spacer(modifier = Modifier.width(8.dp))

            CardInfo(
                modifier = Modifier.weight(1f),
                title = "Максимальный путь за день",
                value = "9.3 км"
            )
        }
    }

}

@Composable
fun CardInfo(modifier: Modifier,
             title: String,
             value: String
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(1f)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = title,
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
