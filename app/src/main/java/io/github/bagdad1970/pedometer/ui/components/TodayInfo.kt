package io.github.bagdad1970.pedometer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import io.github.bagdad1970.pedometer.R


@Composable
fun TodayInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.today_info)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TodayInfoColumn(R.drawable.distance_logo, "1,96", stringResource(id = R.string.kilometers_today))
        TodayInfoColumn(R.drawable.calories_logo, "189", stringResource(id = R.string.kilocalories_today))
        TodayInfoColumn(R.drawable.time_logo, "1:45", stringResource(id = R.string.time_today))
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

