package io.github.bagdad1970.pedometer

import android.content.Intent
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.bagdad1970.pedometer.settings.SettingsActivity
import io.github.bagdad1970.pedometer.todaystats.TodayStatsActivity
import io.github.bagdad1970.pedometer.totalstats.TotalStatsActivity


@Composable
fun BottomNavigationBar(currentScreen: AppActivity) {
    val context = LocalContext.current

    NavigationBar(
        modifier = Modifier.height(64.dp),
        containerColor = Color(145, 116, 230)
    ) {
        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.today_statistics_nav_icon), contentDescription = "today") },
            label = { Text(stringResource(id = R.string.today_stats_nav)) },
            selected = currentScreen == AppActivity.TODAY_STATS,
            onClick = {
                if (currentScreen != AppActivity.TODAY_STATS) {
                    val intent = Intent(context, TodayStatsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    context.startActivity(intent)
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.total_statistics_nav_icon), contentDescription = "total") },
            label = { Text(stringResource(id = R.string.total_stats_nav)) },
            selected = currentScreen == AppActivity.TOTAL_STATS,
            onClick = {
                if (currentScreen != AppActivity.TOTAL_STATS) {
                    val intent = Intent(context, TotalStatsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    context.startActivity(intent)
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "settings") },
            label = { Text(stringResource(id = R.string.settings_nav)) },
            selected = currentScreen == AppActivity.SETTINGS,
            onClick = {
                if (currentScreen != AppActivity.SETTINGS) {
                    val intent = Intent(context, SettingsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    context.startActivity(intent)
                }
            }
        )
    }
}

