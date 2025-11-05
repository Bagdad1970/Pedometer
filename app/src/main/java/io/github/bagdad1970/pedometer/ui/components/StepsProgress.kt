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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import io.github.bagdad1970.pedometer.R


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
            modifier = Modifier.size(dimensionResource(id = R.dimen.steps_progress_indicator_size)),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = dimensionResource(id = R.dimen.steps_progress_indicator_stroke_width),
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

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.steps_progress_spacer)))

            Text(
                text = "${stringResource(id = R.string.target)}: $targetSteps",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.steps_progress_spacer)))

            Text(
                text = "$progressPercent%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.steps_progress_spacer)))

            Button(modifier = Modifier, onClick = onResetSteps) {
                Text(
                    text = stringResource(id = R.string.reset),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}