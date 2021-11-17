package dev.arildo.tuner

import kotlin.math.abs

object Notes {

    private fun getClosestFrequency(pitchHz: Float): Double? {
        val generalList = mutableListOf<Double?>()

        generalList.add(NotesEnum.C.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.C_SHARP.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.D.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.D_SHARP.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.E.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.F.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.F_SHARP.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.G.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.G_SHARP.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.A.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.A_SHARP.value.closestValue(pitchHz.toDouble()))
        generalList.add(NotesEnum.B.value.closestValue(pitchHz.toDouble()))

        return generalList.closestValue(pitchHz.toDouble())
    }

    fun getClosestNote(pitchHz: Float): String {
        val closestFrequency = getClosestFrequency(pitchHz)

        return NotesEnum.getNoteByFrequency(closestFrequency).title
    }

    private fun List<Double?>.closestValue(value: Double) = minByOrNull { abs(value - it!!) }

}