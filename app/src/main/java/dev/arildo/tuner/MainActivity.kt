package dev.arildo.tuner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import dev.arildo.tuner.Notes.getCurrentPitchState

@ExperimentalWearMaterialApi
class MainActivity : ComponentActivity() {

    private val noteState = MutableLiveData<TunerState>()
    private val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)
    private var lastPitchUpdate = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preventAutoLockScreen(window)

        requestMicrophonePermission()

        setContent {
            MaterialTheme {
                Scaffold(
                    timeText = { TimeText() }
                ) {
                    noteState.observeAsState().value?.run { TunerScreen(this) }
                }
            }
        }

        setupAudioListener()
    }

    private fun setupAudioListener() {
        val pdh = PitchDetectionHandler { res, audioEvent ->
            val pitchInHz = res.pitch.toDouble()
            if (shouldUpdateTunerState(pitchInHz, audioEvent)) {
                noteState.postValue(getCurrentPitchState(pitchInHz))
                saveLastUpdatedState()
            }
        }
        val pitchProcessor: AudioProcessor = PitchProcessor(
            PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
            22050F, 1024, pdh
        )
        dispatcher.addAudioProcessor(pitchProcessor)

        val audioThread = Thread(dispatcher, "Audio Thread")
        audioThread.start()
    }

    private fun shouldUpdateTunerState(pitchInHz: Double, audioEvent: AudioEvent): Boolean {
        return audioEvent.getdBSPL() > MIC_THRESHOLD &&
                System.currentTimeMillis() - lastPitchUpdate > 250 &&
                pitchInHz != -1.0
    }

    private fun saveLastUpdatedState() {
        lastPitchUpdate = System.currentTimeMillis()
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

    companion object {
        const val MIC_THRESHOLD = -60
    }
}