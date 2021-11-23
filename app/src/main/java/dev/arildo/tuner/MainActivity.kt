package dev.arildo.tuner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText

@ExperimentalWearMaterialApi
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preventAutoLockScreen(window)

        requestMicrophonePermission()

        viewModel.startAudioListener()

        setContent {
            MaterialTheme {
                Scaffold(
                    timeText = { TimeText() }
                ) {
                    viewModel.tunerState.observeAsState().value?.run { TunerScreen(this) }
                }
            }
        }
    }

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun TunedView() = TunerScreen(TunerState.Tuned(NotesEnum.A))

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun OutOfTuneViewDown() = TunerScreen(TunerState.Down(NotesEnum.A))

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun OutOfTuneViewUp() = TunerScreen(TunerState.Up(NotesEnum.A))

}