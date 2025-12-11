package io.github.bagdad1970.pedometer.settings

import android.content.Context // ← ADD THIS
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import io.github.bagdad1970.pedometer.AppDatabase
import io.github.bagdad1970.pedometer.PedometerApplication
import io.github.bagdad1970.pedometer.dao.UserDao
import io.github.bagdad1970.pedometer.entity.User
import io.github.bagdad1970.pedometer.ui.components.LoginScreen
import io.github.bagdad1970.pedometer.ui.components.RegisterScreen
import io.github.bagdad1970.pedometer.ui.theme.PedometerTheme
import io.github.bagdad1970.pedometer.utils.LocaleHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity : ComponentActivity() {

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
                PedometerTheme {
                    AuthApp(
                        userDao = userDao,
                        onSuccess = { finish() },
                        onBack = { if (it == Auth.LOGIN) finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthApp(
    userDao: UserDao,
    onSuccess: () -> Unit,
    onBack: (Auth) -> Unit
) {
    var authScreen by remember { mutableStateOf(Auth.LOGIN) }

    val context = LocalContext.current
    val sharedPreferences = remember(context) {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                userDao = userDao,
                onRegisterClick = { authScreen = Auth.REGISTER },
                onLoginSuccess = { onSuccess() },
                sharedPreferences = sharedPreferences,
                snackbarHostState = snackbarHostState,

            )

            Auth.REGISTER -> RegisterScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                userDao = userDao,
                onBackToLogin = { authScreen = Auth.LOGIN },
                onRegisterSuccess = { authScreen = Auth.LOGIN },
                sharedPreferences = sharedPreferences,
                snackbarHostState = snackbarHostState

            )
        }
    }
}