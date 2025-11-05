package io.github.bagdad1970.pedometer.ui.components

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import io.github.bagdad1970.pedometer.utils.isValidEmail
import io.github.bagdad1970.pedometer.utils.isValidPassword
import kotlinx.coroutines.launch
import androidx.core.content.edit
import io.github.bagdad1970.pedometer.R

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
            .padding(dimensionResource(id = R.dimen.auth_screen)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text(stringResource(id = R.string.email)) },
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null)
                Text(emailError!!, color = MaterialTheme.colorScheme.error)

            Spacer(Modifier.height(dimensionResource(id = R.dimen.auth_screen_spacer1)))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text(stringResource(id = R.string.password)) },
                isError = passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError != null)
                Text(passwordError!!, color = MaterialTheme.colorScheme.error)

            Spacer(Modifier.height(dimensionResource(id = R.dimen.auth_screen_spacer2)))

            val emailErrorMsg = stringResource(id = R.string.invalid_email)
            val passwordErrorMsg = stringResource(id = R.string.invalid_password)
            val invalidEmailOrPasswordMsg = stringResource(id = R.string.invalid_email_or_password)

            Button(
                onClick = {
                    var valid = true
                    if (!isValidEmail(email)) {
                        emailError = emailErrorMsg
                        valid = false
                    }
                    if (!isValidPassword(password)) {
                        passwordError = passwordErrorMsg
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
                    }
                    else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(invalidEmailOrPasswordMsg)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.login))
            }

            Spacer(Modifier.height(dimensionResource(id = R.dimen.auth_screen_spacer3)))

            TextButton(onClick = onRegisterClick) {
                Text(stringResource(id = R.string.dont_have_an_account))
            }
        }
    }
}