package dv.trubnikov.fourier.circles.models

import kotlin.math.PI

fun Float.toDegree(): Float = 180f * this / PI.toFloat()

fun Float.toRad(): Float = 180f * this / PI.toFloat()
