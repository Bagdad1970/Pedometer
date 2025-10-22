package io.github.bagdad1970.pedometer.ui.components

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.github.bagdad1970.pedometer.utils.isValidEmail
import io.github.bagdad1970.pedometer.utils.isValidPassword
import kotlinx.coroutines.launch
import androidx.core.content.edit

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: (String) -> Unit = {},
    sharedPreferences: SharedPreferences,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val savedEmail = sharedPreferences.getString("user_email", "")
        if (!savedEmail.isNullOrEmpty()) {
            email = savedEmail
        }
    }

    Box(
        modifier = modifier
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text("Email") },
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null)
                Text(emailError!!, color = MaterialTheme.colorScheme.error)

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text("Пароль") },
                isError = passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError != null)
                Text(passwordError!!, color = MaterialTheme.colorScheme.error)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    var valid = true
                    if (!isValidEmail(email)) {
                        emailError = "Некорректный email"
                        valid = false
                    }
                    if (!isValidPassword(password)) {
                        passwordError = "Некорректный пароль"
                        valid = false
                    }

                    if (!valid) return@Button

                    val savedEmail = sharedPreferences.getString("user_email", "")
                    val savedPassword = sharedPreferences.getString("user_password", "")

                    if (email == savedEmail && password == savedPassword) {
                        sharedPreferences.edit {
                            putBoolean("is_logged_in", true)
                        }
                        onLoginSuccess(email)
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Неверный email или пароль")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Войти")
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onRegisterClick) {
                Text("Нет аккаунта? Зарегистрироваться")
            }
        }
    }
}