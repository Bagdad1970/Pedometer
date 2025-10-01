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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import io.github.bagdad1970.pedometer.AppActivity
import io.github.bagdad1970.pedometer.BottomNavigationBar
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.ui.components.PersonalDetailChoice

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences = LocalContext.current.getSharedPreferences("settings", MODE_PRIVATE)

            fun getSex(): Sex {
                val sexString = sharedPreferences.getString("sex", "male") ?: "male"
                return try {
                    Sex.valueOf(sexString)
                } catch (e: IllegalArgumentException) {
                    Sex.MALE
                }
            }
            fun getHeight(): Int {
                return sharedPreferences.getInt("height", 170)
            }
            fun getWeight(): Int {
                return sharedPreferences.getInt("weight", 70)
            }
            fun getAge(): Int {
                return sharedPreferences.getInt("age", 25)
            }

            var height by remember{ mutableIntStateOf(getHeight()) }
            var age by remember{ mutableIntStateOf(getAge()) }
            var weight by remember{ mutableIntStateOf(getWeight()) }
            var sex by remember{ mutableStateOf(getSex()) }

            fun setSex(newSex: Sex) {
                if (sex != newSex) {
                    sex = newSex
                    sharedPreferences.edit { putString("sex", newSex.toString()) }
                }
            }
            fun setHeight(newHeight: Int) {
                if (height != newHeight) {
                    height = newHeight
                    sharedPreferences.edit { putInt("height", newHeight) }
                }
            }
            fun setWeight(newWeight: Int){
                if (weight != newWeight) {
                    weight = newWeight
                    sharedPreferences.edit { putInt("weight", newWeight) }
                }
            }
            fun setAge(newAge: Int){
                if (age != newAge) {
                    age = newAge
                    sharedPreferences.edit { putInt("age", newAge) }
                }
            }

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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PersonalDetailChoice(
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                            detailName = stringResource(id = R.string.sex_name),
                            value = sex,
                            metric = "",
                            onChanged = { newSex -> setSex(newSex as Sex) }
                        )

                        PersonalDetailChoice(
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                            detailName = stringResource(id = R.string.height_name),
                            value = height,
                            metric = stringResource(id = R.string.height_metric),
                            startPickerValue = 60,
                            endPickerValue = 250,
                            onChanged = { newHeight -> setHeight(newHeight as Int) },
                            currentValue = getHeight()
                        )

                        PersonalDetailChoice(
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                            detailName = stringResource(id = R.string.weight_name),
                            value = weight,
                            metric = stringResource(id = R.string.weight_metric),
                            startPickerValue = 40,
                            endPickerValue = 300,
                            onChanged = { newWeight -> setWeight(newWeight as Int) },
                            currentValue = getWeight()
                        )

                        PersonalDetailChoice(
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                            detailName = stringResource(id = R.string.age_name),
                            value = age,
                            metric = stringResource(id = R.string.age_metric),
                            startPickerValue = 12,
                            endPickerValue = 99,
                            onChanged = { newAge -> setAge(newAge as Int) },
                            currentValue = getAge()
                        )
                    }
                }
            }
        }
    }
}