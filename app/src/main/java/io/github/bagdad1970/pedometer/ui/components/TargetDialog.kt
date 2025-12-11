package io.github.bagdad1970.pedometer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import io.github.bagdad1970.pedometer.R


@Composable
fun StepDayTargetChoice(
    modifier: Modifier,
    detailName: String,
    value: Int,
    metric: String? = null,
    startPickerValue: Int = 0,
    endPickerValue: Int = 100000,
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
                DialogWithNumberInput(
                    detailName = detailName,
                    onDismissRequest = { openDialog = false },
                    onConfirmation = { newValue ->
                        onChanged(newValue)
                        openDialog = false
                    },
                    startValue = startPickerValue,
                    endValue = endPickerValue,
                    currentValue = value,
                    metric = metric
                )
            }
        }
    }
}

@Composable
fun DialogWithNumberInput(
    detailName: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (Int) -> Unit,
    startValue: Int,
    endValue: Int,
    currentValue: Int,
    metric: String? = null
) {
    var inputText by remember { mutableStateOf(currentValue.toString()) }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Введите $detailName",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                        error = false
                    },
                    label = { Text(detailName) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    isError = error,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    suffix = { if (metric != null) Text(metric) }
                )
                if (error) {
                    Text(
                        text = "Введите число от $startValue до $endValue",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val number = inputText.toIntOrNull()
                    if (number != null && number >= startValue && number <= endValue) {
                        onConfirmation(number)
                    } else {
                        error = true
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Отмена")
            }
        }
    )
}