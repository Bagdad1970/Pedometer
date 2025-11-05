package io.github.bagdad1970.pedometer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import io.github.bagdad1970.pedometer.R


@Composable
fun NumberPersonalDetailChoice(
    modifier: Modifier,
    detailName: String,
    value: Int,
    metric: String? = null,
    startPickerValue: Int = 0,
    endPickerValue: Int = 350,
    onChanged: (Int) -> Unit,
) {
    var openDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${detailName.replaceFirstChar { it.uppercase() }}:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 18.sp
        )

        Box {
            Row(
                modifier = Modifier
                    .clickable { openDialog = true }
                    .padding(dimensionResource(id = R.dimen.personal_detail_row)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.person_detail_row_horizontal_arrangement))
            ) {
                Text(
                    text = "$value ${metric ?: ""}".trim(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 18.sp
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "${stringResource(id = R.string.choose_detail)} $detailName",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.personal_detail_icon))
                )
            }

            if (openDialog) {
                DialogWithNumberPicker(
                    detailName = detailName,
                    onDismissRequest = { openDialog = false },
                    onConfirmation = { newValue ->
                        onChanged(newValue)
                        openDialog = false
                    },
                    startValue = startPickerValue,
                    endValue = endPickerValue,
                    currentValue = value
                )
            }
        }
    }
}