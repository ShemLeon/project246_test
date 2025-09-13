package com.leoevg.project246.ui.theme

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ModifierInfo

@Composable
fun WaveformBar(
    values: IntArray,
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    osSeek: ((Float) -> Unit)? = null
) {
    val color = Color.White
    val backgroundColor = Color.White.copy(alpha = 0.2f)
    var bars = values.size

    Box(modifier = modifier
        .pointerInput(Unit){
            detectTapGestures{ offset ->
               if (onSeek!=null)
            }
        }
    )
}