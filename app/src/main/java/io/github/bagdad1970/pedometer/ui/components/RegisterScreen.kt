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
import androidx.core.content.edit
import io.github.bagdad1970.pedometer.R
import io.github.bagdad1970.pedometer.utils.isValidEmail
import io.github.bagdad1970.pedometer.utils.isValidPassword
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBackToLogin: () -> Unit = {},
    onRegisterSuccess: (String) -> Unit = {},
    sharedPreferences: SharedPreferences,
    snackbarHostState: SnackbarHostState,
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.auth_screen)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = { Text(stringResource(id = R.string.username)) },
                isError = userNameError != null,
                modifier = Modifier.fillMaxWidth()
            )

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

            Spacer(Modifier.height(dimensionResource(id = R.dimen.auth_screen_spacer1)))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = { Text(stringResource(id = R.string.confirm_password)) },
                isError = confirmPasswordError != null,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmPasswordError != null)
                Text(confirmPasswordError!!, color = MaterialTheme.colorScheme.error)

            Spacer(Modifier.height(dimensionResource(id = R.dimen.auth_screen_spacer2)))

            val enterTheUsernameMsg = stringResource(id = R.string.enter_the_username)
            val emailErrorMsg = stringResource(id = R.string.invalid_email)
            val passwordErrorMsg = stringResource(id = R.string.invalid_password)
            val passwordsAreNotEqualMsg = stringResource(id = R.string.passwords_are_not_equal)

            Button(
                onClick = {
                    var valid = true

                    if (username.isBlank()) {
                        userNameError = enterTheUsernameMsg
                        valid = false
                    }

                    if (!isValidEmail(email)) {
                        emailError = emailErrorMsg
                        valid = false
                    }
                    if (!isValidPassword(password)) {
                        passwordError = passwordErrorMsg
                        valid = false
                    }
                    if (password != confirmPassword) {
                        confirmPasswordError = passwordsAreNotEqualMsg
                        valid = false
                    }

                    if (!valid) return@Button

                    sharedPreferences.edit {
                        putString("user_name", username)
                            .putString("user_email", email)
                            .putString("user_password", password)
                            .putBoolean("is_logged_in", true)
                    }

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("")
                        onRegisterSuccess(email)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.have_an_account))
            }

            Spacer(Modifier.height(dimensionResource(id = R.dimen.auth_screen_spacer3)))

            TextButton(onClick = onBackToLogin) {
                Text(stringResource(id = R.string.have_an_account))
            }
        }
    }
}