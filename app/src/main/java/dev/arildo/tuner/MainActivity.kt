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
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import dev.arildo.tuner.Notes.howMuchIsOutOfTune

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
                    TunerScreen(noteState.observeAsState().value)
                }
            }
        }

        setupAudioListener()
    }

    private fun setupAudioListener() {
        val pdh = PitchDetectionHandler { res, audioEvent ->
            val pitchInHz = res.pitch.toDouble()
            if (audioEvent.getdBSPL() > MIC_THRESHOLD && System.currentTimeMillis() - lastPitchUpdate > 300) {
                noteState.postValue(howMuchIsOutOfTune(pitchInHz))
                lastPitchUpdate = System.currentTimeMillis()
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

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun TunedView() {
        TunerScreen(TunerState.Tuned(NotesEnum.A))
    }

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun OutOfTuneViewDown() {
        TunerScreen(TunerState.Down(NotesEnum.A))
    }

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun OutOfTuneViewUp() {
        TunerScreen(TunerState.Up(NotesEnum.A))
    }

    companion object {
        const val MIC_THRESHOLD = -60
    }
}