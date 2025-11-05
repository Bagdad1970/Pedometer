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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.settings.Sex

@Composable
fun SexChoice(
    modifier: Modifier,
    detailName: String,
    sex: Sex,
    onChanged: (Sex) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

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
                    .clickable { expanded = true }
                    .padding(dimensionResource(id = R.dimen.personal_detail_row)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.person_detail_row_horizontal_arrangement))
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
                    contentDescription = "choose_sex",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.personal_detail_icon))
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
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.person_detail_dropdown_menu_item))
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
                            modifier = Modifier.size(dimensionResource(id = R.dimen.person_detail_dropdown_menu_item_icon))
                        )
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.female),
                            modifier = Modifier.padding(start =dimensionResource(id = R.dimen.person_detail_dropdown_menu_item))
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
                            modifier = Modifier.size(dimensionResource(id = R.dimen.person_detail_dropdown_menu_item_icon))
                        )
                    }
                )
            }
        }
    }
}