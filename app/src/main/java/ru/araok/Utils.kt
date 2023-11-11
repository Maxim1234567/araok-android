package ru.araok

fun milliSecondsToTimer(milliseconds: Int): String {
    var minutesString = ""
    var secondsString = ""

    var minutes = ((milliseconds % (1000 * 60 * 60)) / (1000 * 60)).toInt()
    var seconds = (((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000)).toInt()

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

    return minutesString + ":" + secondsString
}