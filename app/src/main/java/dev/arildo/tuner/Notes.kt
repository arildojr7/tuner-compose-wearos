package dev.arildo.tuner

import androidx.wear.compose.material.ExperimentalWearMaterialApi
import dev.arildo.tuner.NotesEnum.Companion.getClosestFrequencyInAllNotes
import dev.arildo.tuner.NotesEnum.Companion.getNoteByFrequency
import kotlin.math.abs

@ExperimentalWearMaterialApi
object Notes {

    fun getCurrentPitchState(pitchHz: Double): TunerState {
        val closestFrequency = getClosestFrequencyInAllNotes(pitchHz)
        val note = getNoteByFrequency(closestFrequency)

        val diff = if (closestFrequency > pitchHz) {
            abs(closestFrequency - pitchHz).unaryMinus()
        } else {
            abs(pitchHz - closestFrequency)
        }

        return when {
            diff.isInPermittedTolerance() -> TunerState.Tuned(note)
            diff < -0.5 -> TunerState.Down(note)
            else -> TunerState.Up(note)
        }
    }

}