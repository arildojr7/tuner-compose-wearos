package dev.arildo.tuner

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.abs

fun Activity.requestMicrophonePermission() {
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            1234
        )
    }
}

fun List<Double>.closestValue(value: Double) = minByOrNull { abs(value - it) } ?: 0.0

fun Double.isInPermittedTolerance() = this in -0.5..0.5

fun preventAutoLockScreen(window: Window) = window.addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON)