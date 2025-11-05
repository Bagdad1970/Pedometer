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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.bagdad1970.pedometer.R
import java.util.Locale

@Composable
fun WeekInfo() {
    val stepsByDayPerWeek = remember {
        listOf(4609, 5522, 1500, 4980, 10400, 3550, 8907)
    }
    val maxStepsPerDay = stepsByDayPerWeek.maxOrNull() ?: 0

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
                    val boxHeight = steps.toFloat() / maxStepsPerDay.toFloat()

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${String.format(Locale.UK, "%.1f", steps / 1000.0)} т.",
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
                            text = when (index) {
                                0 -> stringResource(id = R.string.monday)
                                1 -> stringResource(id = R.string.tuesday)
                                2 -> stringResource(id = R.string.wednesday)
                                3 -> stringResource(id = R.string.thursday)
                                4 -> stringResource(id = R.string.friday)
                                5 -> stringResource(id = R.string.saturday)
                                else -> stringResource(id = R.string.sunday)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.week_info_week_days))
                        )
                    }

                }
            }
        }
    }
}