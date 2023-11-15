package ru.araok

fun milliSecondsToTimer(milliseconds: Int): String {
    var minutesString = ""
    var secondsString = ""

    var minutes = ((milliseconds % (1000 * 60 * 60)) / (1000 * 60))
    var seconds = (((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000))

    minutesString = if(minutes < 10) {
        "0$minutes"
    } else {
        "$minutes"
    }

    secondsString = if(seconds < 10) {
        "0$seconds"
    } else {
        "$seconds"
    }

    return "$minutesString:$secondsString"
}

fun timerToMilliSeconds(timer: String): Int {
    val minutes = timer.split(":")[0].toInt()
    val seconds = timer.split(":")[1].toInt()

    return minutes * 60 * 1000 + seconds * 1000
}