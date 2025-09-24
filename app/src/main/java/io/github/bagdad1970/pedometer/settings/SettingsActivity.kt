package io.github.bagdad1970.pedometer.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.ui.components.PersonalDetailChoice

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
        PersonalDetailChoice(
            modifier,
            detailName = "пол",
            value = sex,
            metric = "",
            onChanged = { newSex -> sex = newSex as Sex }
        )

        PersonalDetailChoice(
            modifier,
            detailName = "рост",
            value = height,
            metric = "см",
            startPickerValue = 100,
            endPickerValue = 250,
            onChanged = { newHeight -> height = newHeight as Int }
        )

        PersonalDetailChoice(
            modifier,
            detailName = "вес",
            value = weight,
            metric = "кг",
            startPickerValue = 15,
            endPickerValue = 300,
            onChanged = { newWeight -> weight = newWeight as Int }
        )

        PersonalDetailChoice(
            modifier,
            detailName = "возраст",
            value = age,
            metric = "лет",
            startPickerValue = 12,
            endPickerValue = 99,
            onChanged = { newAge -> age = newAge as Int }
        )
    }
}