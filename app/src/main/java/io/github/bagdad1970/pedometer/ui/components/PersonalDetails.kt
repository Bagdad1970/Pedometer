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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.settings.Sex
import java.util.Locale


@Composable
fun SexChoice(
    sex: Sex,
    onChanged: (Sex) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = when (sex) {
                    Sex.MALE -> stringResource(id = R.string.male)
                    Sex.FEMALE -> stringResource(id = R.string.female)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp,
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Выбрать пол",
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.male),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                },
                onClick = {
                    onChanged(Sex.MALE)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.male),
                        contentDescription = "male",
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.female),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                },
                onClick = {
                    onChanged(Sex.FEMALE)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.female),
                        contentDescription = "female",
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun PersonalDetailChoice(
    modifier: Modifier,
    detailName: String,
    value: Any,
    metric: String? = null,
    startPickerValue: Int = 0,
    endPickerValue: Int = 100,
    onChanged: (Any) -> Unit
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

        if (value is Sex) {
            SexChoice(
                sex = value,
                onChanged = onChanged
            )
        }
        else if (value is Int) {
            Box {
                Row(
                    modifier = Modifier
                        .clickable { openDialog = true }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$value ${metric ?: ""}".trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 18.sp
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Выбрать $detailName",
                        modifier = Modifier.size(20.dp)
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
                    )
                }
            }
        }
    }
}