package io.github.bagdad1970.pedometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold (
                modifier = Modifier
                    .fillMaxSize(),
                bottomBar = { BottomNavigationBar(AppActivity.SETTINGS) }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .navigationBarsPadding()
                ) {
                    SettingsView(
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun SettingsView(modifier: Modifier) {
    var height by remember { mutableIntStateOf(177) }
    var weight by remember { mutableIntStateOf(70) }
    var age by remember { mutableIntStateOf(20) }
    var sex by remember { mutableStateOf(Sex.MALE) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SexChoice(modifier, sex, onChanged = { newSex -> sex = newSex })
        HeightChoice(modifier, height, onChanged = { newHeight -> height = newHeight })
        WeightChoice(modifier, weight, onChanged = { newWeight -> weight = newWeight })
        AgeChoice(modifier, age, onChanged = { newAge -> age = newAge })
    }
}

@Composable
fun SexChoice(
    modifier: Modifier,
    sex: Sex,
    onChanged: (Sex) -> Unit // Добавляем callback для изменения значения
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Пол:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    text = when (sex) {
                        Sex.MALE -> "Мужской"
                        Sex.FEMALE -> "Женский"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
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
                            text = "Мужской",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    onClick = {
                        onChanged(Sex.MALE) // Используем callback
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.male),
                            contentDescription = "Мужской",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Женский",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    onClick = {
                        onChanged(Sex.FEMALE) // Используем callback
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.female),
                            contentDescription = "Женский",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }
        }
    }
}


@Composable
fun HeightChoice(
    modifier: Modifier,
    height: Int,
    onChanged: (Int) -> Unit // Добавляем callback
) {
    var openDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Рост:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box {
            Row(
                modifier = Modifier
                    .clickable { openDialog = true }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$height см", // Исправляем отображение текста
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Выбрать рост",
                    modifier = Modifier.size(20.dp)
                )
            }

            if (openDialog) {
                DialogWithNumberPicker(
                    type = "рост",
                    onDismissRequest = { openDialog = false },
                    onConfirmation = { newValue ->
                        onChanged(newValue)
                        openDialog = false
                    },
                    startValue = 100,
                    endValue = 250,
                )
            }
        }
    }
}


@Composable
fun DialogWithNumberPicker(
    type: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (Int) -> Unit,
    startValue: Int,
    endValue: Int
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Ваш $type:",
                    modifier = Modifier.padding(16.dp),
                )

                NumberPicker(
                    startValue = startValue,
                    endValue = endValue,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Отмена")
                    }
                    TextButton(
                        onClick = { onConfirmation(150) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("ОК")
                    }
                }
            }
        }
    }
}


@Composable
fun WeightChoice(
    modifier: Modifier,
    weight: Int,
    onChanged: (Int) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Вес:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box {
            Row(
                modifier = Modifier
                    .clickable { openDialog = true }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$weight кг", // Исправляем отображение текста
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Выбрать вес",
                    modifier = Modifier.size(20.dp)
                )
            }

            if (openDialog) {
                DialogWithNumberPicker(
                    type = "вес",
                    onDismissRequest = { openDialog = false },
                    onConfirmation = { newValue ->
                        onChanged(newValue)
                        openDialog = false
                    },
                    startValue = 30,
                    endValue = 200,
                )
            }
        }
    }
}


@Composable
fun AgeChoice(
    modifier: Modifier,
    age: Int,
    onChanged: (Int) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Возраст:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box {
            Row(
                modifier = Modifier
                    .clickable { openDialog = true }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$age лет", // Исправляем отображение текста
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Выбрать возраст",
                    modifier = Modifier.size(20.dp)
                )
            }

            if (openDialog) {
                DialogWithNumberPicker(
                    type = "возраст",
                    onDismissRequest = { openDialog = false },
                    onConfirmation = { newValue ->
                        onChanged(newValue)
                        openDialog = false
                    },
                    startValue = 1,
                    endValue = 120,
                )
            }
        }
    }
}


@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    startValue: Int,
    endValue: Int,
) {
    Box(
        modifier = modifier
    )
    {
        val shownCount = 3 + 1
        val height = 16.dp

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .padding(5.dp)
                .height(height * shownCount)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to Color.Black,
                            1f to Color.Transparent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        ) {
            item {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = " ",
                    fontSize = 16.sp
                )
            }

            items(endValue - startValue + 1) { index ->
                val heightValue = startValue + index
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    text = "$heightValue",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }

            item {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = " ",
                    fontSize = 16.sp
                )
            }
        }
    }
}