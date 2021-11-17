package dev.arildo.tuner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import dev.arildo.tuner.Notes.getClosestNote
import dev.arildo.tuner.theme.OutOfTune
import dev.arildo.tuner.theme.Tuned

@ExperimentalWearMaterialApi
class MainActivity : ComponentActivity() {

    private val noteState = MutableLiveData<String>()

    companion object {
        const val MIC_THRESHOLD = -60
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission()

        setContent {
            MaterialTheme {
                Scaffold(timeText = { TimeText() }) {
                    Layout(Tuned, noteState.observeAsState().value.orEmpty())
                }
            }
        }

        val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)
        val pdh = PitchDetectionHandler { res, audioEvent ->
            val pitchInHz = res.pitch

            Log.e("RMS >>>> ", audioEvent.getdBSPL().toString())

            if (audioEvent.getdBSPL() > MIC_THRESHOLD) {
                noteState.postValue(getClosestNote(pitchInHz))
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

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1234
            );
        }
    }

    @Composable
    fun Layout(color: Color, letter: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = letter, modifier = Modifier.wrapContentSize(),
                textAlign = TextAlign.Center,
                fontSize = 64.sp
            )
        }

    }

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun TunedView() {
        Layout(Tuned, "A")
    }

    @Preview(widthDp = 200, heightDp = 200)
    @Composable
    fun OutOfTuneView() {
        Layout(OutOfTune, "B")
    }
}