package io.github.bagdad1970.pedometer.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.platform.LocalContext
import io.github.bagdad1970.pedometer.ui.components.LoginScreen
import io.github.bagdad1970.pedometer.ui.components.RegisterScreen

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthApp(
                onSuccess = {
                    finish()
                },
                onBack = {
                    // Обработка нажатия назад из AuthApp
                    if (it == Auth.LOGIN) {
                        finish()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthApp(
    onSuccess: () -> Unit,
    onBack: (Auth) -> Unit
) {
    var authScreen by remember { mutableStateOf(Auth.LOGIN) }

    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (authScreen) {
                            Auth.LOGIN -> "Вход"
                            Auth.REGISTER -> "Регистрация"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Если на экране регистрации - вернуться к логину, иначе закрыть активность
                        if (authScreen == Auth.REGISTER) {
                            authScreen = Auth.LOGIN
                        } else {
                            onBack(Auth.LOGIN)
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        when (authScreen) {
            Auth.LOGIN -> LoginScreen(
                onRegisterClick = {
                    authScreen = Auth.REGISTER
                },
                onLoginSuccess = {
                    onSuccess()
                },
                sharedPreferences = sharedPreferences,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            Auth.REGISTER -> RegisterScreen(
                onBackToLogin = {
                    authScreen = Auth.LOGIN
                },
                onRegisterSuccess = {
                    authScreen = Auth.LOGIN
                },
                sharedPreferences = sharedPreferences,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}