package dev.arildo.tuner.core

import androidx.compose.ui.graphics.Color
import dev.arildo.tuner.ui.theme.OutOfTuneColor
import dev.arildo.tuner.ui.theme.TunedColor

sealed class TunerState(open val note: NotesEnum, val bgColor: Color) {
    class Down(override val note: NotesEnum) : TunerState(note, OutOfTuneColor)
    class Tuned(override val note: NotesEnum) : TunerState(note, TunedColor)
    class Up(override val note: NotesEnum) : TunerState(note, OutOfTuneColor)
}