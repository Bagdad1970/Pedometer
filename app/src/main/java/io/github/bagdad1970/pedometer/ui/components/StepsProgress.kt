package io.github.bagdad1970.pedometer.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun StepsProgress(
    modifier: Modifier = Modifier,
    currentSteps: Int,
    targetSteps: Int,
    onResetSteps: () -> Unit,
    shouldAnimate: Boolean = true
) {
    val durationMillis = 800
    val delayMillis = 250

    var startAnimation by remember { mutableStateOf(false) }
    var animatedSteps by remember { mutableIntStateOf(currentSteps) }
    val progress = currentSteps.toFloat() / targetSteps.toFloat()

    LaunchedEffect(key1 = shouldAnimate, key2 = currentSteps) {
        if (shouldAnimate) {
            startAnimation = true
        }
        animatedSteps = currentSteps
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) progress else 0f,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis
        ),
        label = "progress_animation"
    )

    val animatedStepCount by animateIntAsState(
        targetValue = animatedSteps,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis
        ),
        label = "step_count_animation"
    )

    val progressPercent = (animatedProgress * 100).toInt()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(350.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 14.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$animatedStepCount",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Цель: $targetSteps",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "$progressPercent%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(modifier = Modifier, onClick = onResetSteps) {
                Text(
                    text = "Сбросить",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}