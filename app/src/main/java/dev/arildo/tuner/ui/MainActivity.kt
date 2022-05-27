package dev.arildo.tuner.ui

import android.Manifest.permission.RECORD_AUDIO
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dev.arildo.tuner.core.NotesEnum
import dev.arildo.tuner.core.TunerState
import dev.arildo.tuner.core.preventAutoLockScreen
import dev.arildo.tuner.viewmodel.MainViewModel

@ExperimentalWearMaterialApi
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preventAutoLockScreen(window)

        setContent {
            val micPermissionState = rememberPermissionState(RECORD_AUDIO)

            MaterialTheme {
                when (micPermissionState.status) {
                    is PermissionStatus.Granted -> {
                        Scaffold(timeText = { TimeText() }) {
                            TunerScreen(viewModel.tunerState.collectAsState().value)
                        }
                    }
                    is PermissionStatus.Denied -> {
                        WithoutPermissionScreen { micPermissionState.launchPermissionRequest() }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startAudioListener(this)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopAudioListener()
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

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun NoSound() = TunerScreen(TunerState.NoSound)

}