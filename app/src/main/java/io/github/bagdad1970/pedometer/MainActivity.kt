package io.github.bagdad1970.pedometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PedometerTheme {
                MainScreen()
            }
        }
    }
}

data class NavigationItem(
    val title: String,
    val iconResId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("Сегодня", R.drawable.today_statistics_nav_icon),
        NavigationItem("Отчет", R.drawable.total_statistics_nav_icon),
        NavigationItem("Профиль", R.drawable.profile_nav_icon)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconResId),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> TodayStatisticsScreen()
                1 -> TotalStatisticsScreen()
                2 -> ProfileScreen()
            }
        }
    }
}

@Composable
fun TodayStatisticsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepsProgressIndicator(
            currentSteps = 7543,
            targetSteps = 10000,
            modifier = Modifier.padding(16.dp)
        )
        TodayInfo()
    }
}

@Composable
fun TodayInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TodayInfoColumn(R.drawable.distance_logo, "1,96", "км")
        TodayInfoColumn(R.drawable.calories_logo, "189", "ккал")
        TodayInfoColumn(R.drawable.time_logo, "1:45", "Время")
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
            .padding(8.dp)
            .width(60.dp)
            .height(120.dp)

    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = type,
            modifier = Modifier.size(30.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = type,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun TotalStatisticsScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Общая статистика",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Еженедельный прогресс",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp)
                .align(Alignment.Start),
            fontWeight = FontWeight.SemiBold
        )

        WeeklyProgressChart()

        Text(
            text = "Рекорды",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp)
                .align(Alignment.Start),
            fontWeight = FontWeight.SemiBold
        )

        RecordsCard(
            items = listOf(
                RecordItem("Макс. шагов в день", "25,643", "15.12.2023"),
                RecordItem("Самая длинная прогулка", "18.7 км", "22.08.2023"),
                RecordItem("Макс. скорость", "8.2 км/ч", "03.07.2023")
            )
        )

    }
}

data class RecordItem(val title: String, val value: String, val date: String)


@Composable
fun WeeklyProgressChart() {
    val weekData = remember {
        listOf(12000, 8500, 15600, 9800, 13400, 11000, 8900)
    }
    val maxValue = weekData.maxOrNull() ?: 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
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

                Button(
                    onClick = { },
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(text = "За все время")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weekData.forEachIndexed { index, value ->
                    val heightPercent = value.toFloat() / maxValue.toFloat()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (value / 1000).toString() + "k",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height((heightPercent * 80).dp)
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


@Composable
fun RecordsCard(
    items: List<RecordItem>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            items.forEach { item ->
                RecordRow(item = item)
                if (item != items.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )
                }
            }
        }
    }
}

@Composable
fun RecordRow(item: RecordItem) {
    Column {
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = item.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Аватар",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "Фрэнк Каупервуд",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            fontWeight = FontWeight.Medium
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Рост: 190 см",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Вес: 65 кг",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
fun StepsProgressIndicator(
    currentSteps: Int,
    targetSteps: Int,
    modifier: Modifier = Modifier
) {
    val progress = currentSteps.toFloat() / targetSteps.toFloat()
    val progressPercent = (progress * 100).toInt()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(350.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 14.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$currentSteps",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Цель: $targetSteps",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "$progressPercent%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    PedometerTheme {
        MainScreen()
    }
}