package io.github.bagdad1970.pedometer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun WeekInfo() {
    val stepsByDayPerWeek = remember {
        listOf(4609, 5522, 1500, 4980, 10400, 3550, 8907)
    }
    val maxStepsPerDay = stepsByDayPerWeek.maxOrNull() ?: 0

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
}