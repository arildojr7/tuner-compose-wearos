package dev.arildo.tuner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalWearMaterialApi
class MainViewModel : ViewModel() {

    private val _tunerState = MutableLiveData<TunerState>()
    val tunerState = Transformations.map(_tunerState) { it }

    private var lastPitchUpdate = 0L

    fun startAudioListener() = viewModelScope.launch(Dispatchers.Default) {
        val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(
            SAMPLE_RATE, BUFFER_SIZE, BUFFER_OVERLAP
        )

        val pitchProcessor = PitchProcessor(
            PitchEstimationAlgorithm.FFT_YIN,
            SAMPLE_RATE.toFloat(),
            BUFFER_SIZE,
            pitchDetectionHandler()
        )

        dispatcher.addAudioProcessor(pitchProcessor)
        dispatcher.run()
    }

    private fun pitchDetectionHandler() = PitchDetectionHandler { result, audioEvent ->
        val pitchInHz = result.pitch.toDouble()
        if (shouldUpdateTunerState(pitchInHz, audioEvent)) {
            val capturedNoteState = Notes.getCurrentPitchState(pitchInHz)
            _tunerState.postValue(capturedNoteState)

            saveLastUpdatedTime()
        }
    }

    private fun shouldUpdateTunerState(pitchInHz: Double, audioEvent: AudioEvent): Boolean {
        return audioEvent.getdBSPL() > MIC_THRESHOLD && pitchInHz != INVALID_PITCH &&
                System.currentTimeMillis() - lastPitchUpdate > MIN_UPDATE_TIME
    }

    private fun saveLastUpdatedTime() {
        lastPitchUpdate = System.currentTimeMillis()
    }

    companion object {
        const val MIC_THRESHOLD = -60
        const val MIN_UPDATE_TIME = 250
        const val INVALID_PITCH = -1.0
        const val SAMPLE_RATE = 22050
        const val BUFFER_SIZE = 1024
        const val BUFFER_OVERLAP = 0
    }
}