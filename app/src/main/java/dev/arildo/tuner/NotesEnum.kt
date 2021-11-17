package dev.arildo.tuner

enum class NotesEnum(val title: String, val value: List<Double>) {
    C("C", listOf(65.41, 130.8, 261.6, 523.3, 1047.0, 2093.0)),
    C_SHARP("C#", listOf(69.3, 138.6, 277.2, 554.4, 1109.0, 2217.0)),

    D("D", listOf(73.42, 146.8, 293.7, 587.3, 1175.0, 2349.0)),
    D_SHARP("D#", listOf(77.78, 155.6, 311.1, 622.3, 1245.0, 2489.0)),

    E("E", listOf(82.41, 164.8, 329.6, 659.3, 1319.0, 2637.0)),

    F("F", listOf(87.31, 174.6, 349.2, 698.5, 1397.0, 2794.0)),
    F_SHARP("F#", listOf(92.5, 185.0, 370.0, 740.0, 1480.0, 2960.0)),

    G("G", listOf(98.0, 196.0, 392.0, 784.0, 1568.0, 3136.0)),
    G_SHARP("G#", listOf(103.8, 207.7, 415.3, 830.6, 1661.0, 3322.0)),

    A("A", listOf(110.0, 220.0, 330.0, 440.0, 880.0, 1760.0, 3520.0)),
    A_SHARP("A#", listOf(116.5, 233.1, 466.2, 932.3, 1865.0, 3729.0)),

    B("B", listOf(123.5, 246.9, 493.9, 987.8, 1976.0, 3951.0)),

    NONE("", emptyList());

    companion object {
        fun getNoteByFrequency(frequency: Double?): NotesEnum {
            values().forEach {
                if (it.value.contains(frequency)) {
                    return it
                }
            }
            return NONE
        }
    }
}