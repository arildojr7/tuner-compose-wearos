package dev.arildo.tuner.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import dev.arildo.tuner.R
import dev.arildo.tuner.core.TunerState

@Composable
fun TunerScreen(tunerState: TunerState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tunerState.bgColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LeftArrow(isVisible = tunerState is TunerState.Down)
            TextNote(note = tunerState.note.title)
            RightArrow(isVisible = tunerState is TunerState.Up)
        }
    }
}

@Composable
private fun TextNote(note: String?) {
    Text(
        text = note.orEmpty(),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        fontSize = 64.sp,
        modifier = Modifier
            .width(84.dp)
            .wrapContentHeight()
    )
}

@Composable
private fun LeftArrow(isVisible: Boolean) {
    Box(modifier = Modifier.size(56.dp)) {
        AnimatedVisibility(visible = isVisible, enter = fadeIn(), exit = fadeOut()) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun RightArrow(isVisible: Boolean) {
    Box(modifier = Modifier.size(56.dp)) {
        AnimatedVisibility(visible = isVisible, enter = fadeIn(), exit = fadeOut()) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .rotate(180F)
            )
        }
    }
}