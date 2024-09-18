package io.github.hanihashemi.eggmaster.extensions

fun Int.formatSecondsToMinutes(): String {
    val minutes = this / 60
    val remainingSeconds = this % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}