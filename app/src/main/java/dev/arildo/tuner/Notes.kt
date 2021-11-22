package dev.arildo.tuner

import androidx.wear.compose.material.ExperimentalWearMaterialApi
import dev.arildo.tuner.NotesEnum.Companion.getClosestFrequencyInAllNotes
import kotlin.math.abs

@ExperimentalWearMaterialApi
object Notes {

    fun howMuchIsOutOfTune(pitchHz: Double): TunerState {
        val closestFrequency = getClosestFrequencyInAllNotes(pitchHz)
        val closestNote = getClosestNote(pitchHz)

        val diff: Double = if (closestFrequency > pitchHz) {
            abs(closestFrequency - pitchHz).unaryMinus()
        } else {
            abs(pitchHz - closestFrequency)
        }

        return when {
            diff.isInPermittedTolerance() -> {
                TunerState.Tuned(closestNote)
            }
            diff < -1.0 -> {
                TunerState.Down(closestNote)
            }
            else -> {
                TunerState.Up(closestNote)
            }
        }
    }

    private fun getClosestNote(pitchHz: Double): NotesEnum {
        val closestFrequency = getClosestFrequencyInAllNotes(pitchHz)

        return NotesEnum.getNoteByFrequency(closestFrequency)
    }
}