package io.github.bagdad1970.pedometer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun DialogWithNumberPicker(
    detailName: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (Int) -> Unit,
    startValue: Int,
    endValue: Int,
    currentValue: Int?,
) {
    var selectedValue by remember { mutableIntStateOf(currentValue ?: startValue) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Ваш $detailName:",
                    modifier = Modifier
                        .padding(8.dp),
                )

                NumberPicker(
                    startValue = startValue,
                    endValue = endValue,
                    onValueChanged = { newValue ->
                        selectedValue = newValue
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Отмена")
                    }
                    TextButton(
                        onClick = { onConfirmation(selectedValue) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("ОК")
                    }
                }
            }
        }
    }
}


@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    startValue: Int,
    endValue: Int,
    onValueChanged: (Int) -> Unit
) {
    Box(
        modifier = modifier
    )
    {
        val shownCount = 3 + 1

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .padding(10.dp)
                .height(16.dp * shownCount)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to Color.Black,
                            1f to Color.Transparent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        ) {
            item {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = " ",
                    fontSize = 16.sp
                )
            }

            items(endValue - startValue + 1) { index ->
                val value = startValue + index
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable {
                            onValueChanged(value)
                        },
                    text = "$value",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }

            item {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = " ",
                    fontSize = 16.sp
                )
            }
        }
    }
}