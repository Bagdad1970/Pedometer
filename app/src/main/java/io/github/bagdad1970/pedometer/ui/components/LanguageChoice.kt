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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LanguageChoice(
    modifier: Modifier,
    detailName: String,
    language: String? = "EN",
    onChanged: (String) -> Unit
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
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = when (language) {
                        "EN" -> "English"
                        "RU" -> "Русский"
                        else -> { "EN" }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 18.sp,
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Выбрать язык",
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
                            text = "English",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    onClick = {
                        onChanged("EN")
                        expanded = false
                    },
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Русский",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    onClick = {
                        onChanged("RU")
                        expanded = false
                    },
                )
            }
        }
    }
}