// File: app/src/main/java/io/github/bagdad1970/pedometer/settings/SettingsActivity.kt

package io.github.bagdad1970.pedometer.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.edit
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.todaystats.TodayStatsActivity
import io.github.bagdad1970.pedometer.ui.components.LanguageChoice
import io.github.bagdad1970.pedometer.ui.components.NumberPersonalDetailChoice
import io.github.bagdad1970.pedometer.ui.components.SexChoice
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import io.github.bagdad1970.pedometer.utils.LocaleHelper


class SettingsActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.setLocale(it, LocaleHelper.getLanguage(it)) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PedometerTheme {
                val todaySteps = intent.getIntExtra(TodayStatsActivity.EXTRA_TODAY_STEPS, 0)
                val targetSteps = intent.getIntExtra(TodayStatsActivity.EXTRA_TARGET_STEPS, 10000)
                SettingsScreen(
                    todaySteps = todaySteps,
                    targetSteps = targetSteps,
                    onLanguageChanged = { newLanguageCode ->
                        LocaleHelper.setLocale(this, newLanguageCode)
                        recreate()
                    }
                )
            }
        }
    }

}

@Composable
fun SettingsScreen(
    todaySteps: Int,
    targetSteps: Int,
    onLanguageChanged: (String) -> Unit
) {
    val detailPrefs = LocalContext.current.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val userPrefs = LocalContext.current.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun getSex(): Sex {
        val sexString = detailPrefs.getString("sex", "MALE") ?: "MALE"
        return try {
            Sex.valueOf(sexString)
        } catch (e: IllegalArgumentException) {
            Sex.MALE
        }
    }

    fun getLanguage(): String {
        return detailPrefs.getString("language", "EN") ?: "EN"
    }

    fun getHeight(): Int {
        return detailPrefs.getInt("height", 170)
    }

    fun getWeight(): Int {
        return detailPrefs.getInt("weight", 70)
    }

    fun getAge(): Int {
        return detailPrefs.getInt("age", 25)
    }

    fun getUserName(): String {
        return userPrefs.getString("user_name", "") ?: ""
    }

    var height by remember { mutableIntStateOf(getHeight()) }
    var age by remember { mutableIntStateOf(getAge()) }
    var weight by remember { mutableIntStateOf(getWeight()) }
    var sex by remember { mutableStateOf(getSex()) }
    var userName by remember { mutableStateOf(getUserName()) }
    var language by remember { mutableStateOf(getLanguage()) }

    fun setSex(newSex: Sex) {
        if (sex != newSex) {
            sex = newSex
            detailPrefs.edit { putString("sex", newSex.toString()) }
        }
    }

    fun setLanguage(newLanguage: String) {
        if (language != newLanguage) {
            language = newLanguage
            detailPrefs.edit { putString("language", newLanguage) }
        }
    }

    fun setHeight(newHeight: Int) {
        if (height != newHeight) {
            height = newHeight
            detailPrefs.edit { putInt("height", newHeight) }
        }
    }

    fun setWeight(newWeight: Int) {
        if (weight != newWeight) {
            weight = newWeight
            detailPrefs.edit { putInt("weight", newWeight) }
        }
    }

    fun setAge(newAge: Int) {
        if (age != newAge) {
            age = newAge
            detailPrefs.edit { putInt("age", newAge) }
        }
    }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(AppActivity.SETTINGS) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val shareText = when (language) {
                        "RU" -> "За сегодня у меня уже $todaySteps из $targetSteps"
                        else -> "Today I have $todaySteps out of $targetSteps steps"
                    }
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent,
                        if (language == "RU") "Поделиться" else "Share"))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "share"
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.settings_screen_column)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, AuthActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.settings_screen_button_vertical))
                ) {
                    Text(stringResource(id = R.string.sign_in))
                }

                val isLoggedIn = userPrefs.getBoolean("is_logged_in", false)
                if (isLoggedIn && getUserName().isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(id = R.dimen.settings_screen_card_bottom))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(id = R.dimen.settings_screen_card_column_bottom)),
                            horizontalAlignment = Alignment.Start
                        ) {
                            if (userName.isNotEmpty()) {
                                Text(
                                    text = "${stringResource(id = R.string.greetings)}: $userName",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.settings_screen_card_text_bottom))
                                )
                            }
                        }
                    }
                }

                LanguageChoice(
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                    detailName = stringResource(id = R.string.language_name),
                    language = language,
                    onChanged = { newLanguage -> setLanguage(newLanguage) },
                    onLanguageChanged = onLanguageChanged
                )

                SexChoice(
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                    detailName = stringResource(id = R.string.sex_name),
                    sex = sex,
                    onChanged = { newSex -> setSex(newSex) }
                )

                NumberPersonalDetailChoice(
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                    detailName = stringResource(id = R.string.height_name),
                    value = height,
                    metric = stringResource(id = R.string.height_metric),
                    startPickerValue = 60,
                    endPickerValue = 250,
                    onChanged = { newHeight -> setHeight(newHeight) },
                )

                NumberPersonalDetailChoice(
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                    detailName = stringResource(id = R.string.weight_name),
                    value = weight,
                    metric = stringResource(id = R.string.weight_metric),
                    startPickerValue = 40,
                    endPickerValue = 300,
                    onChanged = { newWeight -> setWeight(newWeight) },
                )

                NumberPersonalDetailChoice(
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                    detailName = stringResource(id = R.string.age_name),
                    value = age,
                    metric = stringResource(id = R.string.age_metric),
                    startPickerValue = 12,
                    endPickerValue = 99,
                    onChanged = { newAge -> setAge(newAge) },
                )
            }
        }
    }
}