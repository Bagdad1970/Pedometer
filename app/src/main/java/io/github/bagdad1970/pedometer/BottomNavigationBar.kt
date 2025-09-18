package io.github.bagdad1970.pedometer

import android.content.Intent
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun BottomNavigationBar(currentScreen: AppActivity) {
    val context = LocalContext.current

    NavigationBar(
        modifier = Modifier.height(64.dp),
        containerColor = Color(145, 116, 230)
    ) {
        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.today_statistics_nav_icon), contentDescription = "Сегодня") },
            label = { Text("Сегодня") },
            selected = currentScreen == AppActivity.TODAY_STATS,
            onClick = {
                if (currentScreen != AppActivity.TODAY_STATS) {
                    context.startActivity(Intent(context, TodayStatsActivity::class.java))
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.total_statistics_nav_icon), contentDescription = "Отчет") },
            label = { Text("Главная") },
            selected = currentScreen == AppActivity.TOTAL_STATS,
            onClick = {
                if (currentScreen != AppActivity.TOTAL_STATS) {
                    context.startActivity(Intent(context, TotalStatsActivity::class.java))
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.profile_nav_icon), contentDescription = "Профиль") },
            label = { Text("Профиль") },
            selected = currentScreen == AppActivity.PROFILE,
            onClick = {
                if (currentScreen != AppActivity.PROFILE) {
                    context.startActivity(Intent(context, ProfileActivity::class.java))
                }
            }
        )
    }
}

