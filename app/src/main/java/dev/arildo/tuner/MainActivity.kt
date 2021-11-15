package dev.arildo.tuner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import dev.arildo.tuner.theme.OutOfTune
import dev.arildo.tuner.theme.Tuned

@ExperimentalWearMaterialApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Scaffold(
                    timeText = {
                        TimeText()
                    }
                ) {
                    Layout(Tuned)
                }
            }
        }
    }
}

@Composable
fun Layout(color: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "B", modifier = Modifier.wrapContentSize(),
            textAlign = TextAlign.Center,
            fontSize = 84.sp
        )
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
fun TunedView() {
    Layout(Tuned)
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
fun OutOfTuneView() {
    Layout(OutOfTune)
}