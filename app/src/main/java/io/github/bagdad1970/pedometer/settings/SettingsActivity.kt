package io.github.bagdad1970.pedometer.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.PedometerApplication
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.dao.UserDao
import io.github.bagdad1970.pedometer.entity.User
import io.github.bagdad1970.pedometer.ui.components.LanguageChoice
import io.github.bagdad1970.pedometer.ui.components.NumberPersonalDetailChoice
import io.github.bagdad1970.pedometer.ui.components.SexChoice
import io.github.bagdad1970.pedometer.ui.components.StepDayTargetChoice
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import io.github.bagdad1970.pedometer.utils.Calculations
import io.github.bagdad1970.pedometer.utils.LocaleHelper
import kotlinx.coroutines.launch


class SettingsActivity : ComponentActivity() {

    private lateinit var userDao: UserDao

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.setLocale(it, LocaleHelper.getLanguage(it)) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as PedometerApplication
        userDao = app.database.userDao()

        setContent {
            PedometerTheme {
//                val todaySteps = intent.getIntExtra(TodayStatsActivity.EXTRA_TODAY_STEPS, 0)
//                val targetSteps = intent.getIntExtra(TodayStatsActivity.EXTRA_TARGET_STEPS, 10000)
                SettingsScreen(
                    userDao = userDao,
                    todaySteps = 0,
                    targetSteps = 10000,
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
    userDao: UserDao,
    todaySteps: Int,
    targetSteps: Int,
    onLanguageChanged: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPrefs = LocalContext.current.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val userEmail = userPrefs.getString("user_email", null)

    var currentUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(userEmail) {
        if (!userEmail.isNullOrBlank()) {
            try {
                currentUser = userDao.findByEmail(userEmail)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val user = currentUser
    var height by remember(user) { mutableIntStateOf(user?.height ?: 170) }
    var weight by remember(user) { mutableIntStateOf(user?.weight ?: 70) }
    var age by remember(user) { mutableIntStateOf(user?.age ?: 25) }
    var sex by remember(user) { mutableStateOf(user?.sex ?: Sex.MALE) }
    var language by remember(user) { mutableStateOf((user?.language ?: "EN").uppercase()) }
    var target by remember(user) { mutableIntStateOf(user?.target ?: 10000) }

    fun updateStepLength() {
        if (user == null) return
        val newStepLength = Calculations.calculateStepLength(height, sex)
        coroutineScope.launch {
            userDao.updateStepLength(user.id, newStepLength)
        }
    }

    fun updateHeight(newHeight: Int) {
        if (height == newHeight || user == null) return
        height = newHeight
        updateStepLength()
        coroutineScope.launch {
            userDao.updateHeight(user.id, newHeight)
        }
    }

    fun updateWeight(newWeight: Int) {
        if (weight == newWeight || user == null) return
        weight = newWeight
        coroutineScope.launch {
            userDao.updateWeight(user.id, newWeight)
        }
    }

    fun updateAge(newAge: Int) {
        if (age == newAge || user == null) return
        age = newAge
        coroutineScope.launch {
            userDao.updateAge(user.id, newAge)
        }
    }

    fun updateSex(newSex: Sex) {
        if (sex == newSex || user == null) return
        sex = newSex
        updateStepLength()
        coroutineScope.launch {
            userDao.updateSex(user.id, newSex)
        }
    }

    fun updateLanguage(newLanguage: String) {
        val newLang = newLanguage.uppercase()
        if (language == newLang || user == null) return
        language = newLang
        coroutineScope.launch {
            userDao.updateLanguage(user.id, newLang)
        }
        onLanguageChanged(newLang)
    }

    fun updateTarget(newTarget: Int) {
        if (target == newTarget || user == null) return
        target = newTarget
        coroutineScope.launch {
            userDao.updateTarget(user.id, newTarget)
        }
    }

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
                Icon(imageVector = Icons.Default.Share, contentDescription = "share")
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
                    .padding(dimensionResource(id = R.dimen.settings_screen_column))
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            context.startActivity(
                                Intent(context, AuthActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(id = R.dimen.settings_screen_button_vertical))
                    ) {
                        Text(stringResource(id = R.string.sign_in))
                    }

                    val isLoggedIn = userPrefs.getBoolean("is_logged_in", false)
                    val userName = userPrefs.getString("user_name", "") ?: ""
                    if (isLoggedIn && userName.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                        ) {
                            Text(
                                text = "${stringResource(id = R.string.greetings)}, $userName",
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    LanguageChoice(
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                        detailName = stringResource(id = R.string.language_name),
                        language = language,
                        onChanged = { lang -> updateLanguage(lang) },
                        onLanguageChanged = onLanguageChanged
                    )

                    SexChoice(
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                        detailName = stringResource(id = R.string.sex_name),
                        sex = sex,
                        onChanged = { newSex -> updateSex(newSex) }
                    )

                    NumberPersonalDetailChoice(
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                        detailName = stringResource(id = R.string.height_name),
                        value = height,
                        metric = stringResource(id = R.string.height_metric),
                        startPickerValue = 60,
                        endPickerValue = 250,
                        onChanged = { newHeight -> updateHeight(newHeight) }
                    )

                    NumberPersonalDetailChoice(
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                        detailName = stringResource(id = R.string.weight_name),
                        value = weight,
                        metric = stringResource(id = R.string.weight_metric),
                        startPickerValue = 40,
                        endPickerValue = 300,
                        onChanged = { newWeight -> updateWeight(newWeight) }
                    )

                    NumberPersonalDetailChoice(
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                        detailName = stringResource(id = R.string.age_name),
                        value = age,
                        metric = stringResource(id = R.string.age_metric),
                        startPickerValue = 12,
                        endPickerValue = 99,
                        onChanged = { newAge -> updateAge(newAge) }
                    )

                    StepDayTargetChoice(
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.personal_detail_margin_bottom)),
                        detailName = stringResource(id = R.string.target_name),
                        value = target,
                        metric = stringResource(id = R.string.target_metric),
                        startPickerValue = 0,
                        endPickerValue = 100000,
                        onChanged = { newTarget -> updateTarget(newTarget) }
                    )
                }
            }
        }
    }
}