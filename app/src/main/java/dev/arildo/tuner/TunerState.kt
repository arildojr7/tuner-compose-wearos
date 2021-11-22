package dev.arildo.tuner

import androidx.compose.ui.graphics.Color
import dev.arildo.tuner.theme.OutOfTuneColor
import dev.arildo.tuner.theme.TunedColor

sealed class TunerState(open val note: NotesEnum, val bgColor: Color) {
    class Down(override val note: NotesEnum) : TunerState(note, OutOfTuneColor)
    class Tuned(override val note: NotesEnum) : TunerState(note, TunedColor)
    class Up(override val note: NotesEnum) : TunerState(note, OutOfTuneColor)
}