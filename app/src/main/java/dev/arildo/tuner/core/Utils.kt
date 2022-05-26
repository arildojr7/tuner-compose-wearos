package dev.arildo.tuner.core

import android.view.Window
import android.view.WindowManager.LayoutParams
import kotlin.math.abs

fun List<Double>.closestValue(value: Double) = minByOrNull { abs(value - it) } ?: 0.0

fun Double.isInPermittedTolerance() = this in -0.5..0.5

fun preventAutoLockScreen(window: Window) = window.addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON)