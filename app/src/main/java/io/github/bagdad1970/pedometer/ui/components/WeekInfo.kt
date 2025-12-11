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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.bagdad1970.pedometer.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

@Composable
fun WeekInfo(
    stepsByDayPerWeek: List<Int>
) {
    val maxStepsPerDay = stepsByDayPerWeek.maxOrNull() ?: 1
    val today = LocalDate.now()

    val weekDays = getLast7WeekDays(today)

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = dimensionResource(id = R.dimen.week_info_card))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.week_info_card_column))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.last_week),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.week_info_card_steps_by_days)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                stepsByDayPerWeek.forEachIndexed { index, steps ->
                    val boxHeight = if (maxStepsPerDay > 0) {
                        steps.toFloat() / maxStepsPerDay.toFloat()
                    } else {
                        0f
                    }

                    // Получаем день недели для текущего индекса
                    val dayOfWeek = weekDays.getOrNull(index) ?: DayOfWeek.MONDAY

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val stepsInK = if (steps > 0) {
                            String.format(Locale.UK, "%.1f", steps / 1000.0)
                        } else {
                            "0"
                        }

                        Text(
                            text = "$stepsInK т.",
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = dimensionResource(id = R.dimen.week_info_card_steps_by_day_text))
                        )

                        Box(
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.week_info_card_column_width))
                                .height((boxHeight * 80).dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.week_info_card_column_shape))
                                )
                        )

                        Text(
                            text = getDayOfWeekShortName(dayOfWeek),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.week_info_week_days))
                        )
                    }

                }
            }
        }
    }
}

private fun getLast7WeekDays(today: LocalDate): List<DayOfWeek> {
    return (0..6).map { today.minusDays(it.toLong()) }
        .reversed()
        .map { it.dayOfWeek }
}

@Composable
private fun getDayOfWeekShortName(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(id = R.string.monday)
        DayOfWeek.TUESDAY -> stringResource(id = R.string.tuesday)
        DayOfWeek.WEDNESDAY -> stringResource(id = R.string.wednesday)
        DayOfWeek.THURSDAY -> stringResource(id = R.string.thursday)
        DayOfWeek.FRIDAY -> stringResource(id = R.string.friday)
        DayOfWeek.SATURDAY -> stringResource(id = R.string.saturday)
        DayOfWeek.SUNDAY -> stringResource(id = R.string.sunday)
    }
}