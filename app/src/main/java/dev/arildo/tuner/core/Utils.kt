package dev.arildo.tuner.core

import android.Manifest.permission.RECORD_AUDIO
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.core.content.ContextCompat.checkSelfPermission
import kotlin.math.abs

fun hasMicrophonePermission(context: Context) =
    checkSelfPermission(context, RECORD_AUDIO) == PERMISSION_GRANTED

fun List<Double>.closestValue(value: Double) = minByOrNull { abs(value - it) } ?: 0.0

fun Double.isInPermittedTolerance() = this in -0.5..0.5

fun preventAutoLockScreen(window: Window) = window.addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON)