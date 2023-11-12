package ru.araok

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val timer = "02:34"
        val millseconds = 2 * 60 * 1000 + 34 * 1000

        Log.d("", "millseconds: $millseconds")
        Log.d("", "timerToMilliSeconds: ${timerToMilliSeconds(timer)}")
    }
}

fun timerToMilliSeconds(timer: String): Int {
    val minutes = timer.split(":")[0].toInt()
    val seconds = timer.split(":")[1].toInt()

    return minutes * 60 * 1000 + seconds * 1000
}