package com.desipartygames.core

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.ToneGenerator
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

object SoundEffects {
    private var isSoundEnabled = true
    private var isVibrationEnabled = true

    fun setSoundEnabled(enabled: Boolean) {
        isSoundEnabled = enabled
    }

    fun isSoundOn(): Boolean = isSoundEnabled

    fun setVibrationEnabled(enabled: Boolean) {
        isVibrationEnabled = enabled
    }

    fun isVibrationOn(): Boolean = isVibrationEnabled

    fun playTick(context: Context) {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 60)
                toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
                toneGen.release()
            } catch (_: Exception) {}
        }
    }

    fun playWarningTick(context: Context) {
        if (isVibrationEnabled) vibrateShort(context, 40)
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
                toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 80)
                toneGen.release()
            } catch (_: Exception) {}
        }
    }

    fun playBuzzer(context: Context) {
        if (isVibrationEnabled) vibrateLong(context, 350)
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                playSynthesizedTone(frequency = 180.0, durationMs = 400, volume = 0.8f)
            } catch (_: Exception) {
                try {
                    val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 80)
                    toneGen.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 300)
                    toneGen.release()
                } catch (_: Exception) {}
            }
        }
    }

    fun playSuccess(context: Context) {
        if (isVibrationEnabled) vibrateShort(context, 80)
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                playArpeggio(listOf(523.25, 659.25, 783.99, 1046.50), noteDurationMs = 90)
            } catch (_: Exception) {}
        }
    }

    fun playFanfare(context: Context) {
        if (isVibrationEnabled) vibratePattern(context, longArrayOf(0, 100, 50, 100, 50, 250))
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                playArpeggio(listOf(440.0, 554.37, 659.25, 880.0, 1108.73), noteDurationMs = 120)
            } catch (_: Exception) {}
        }
    }

    fun playChitShuffle(context: Context) {
        if (isVibrationEnabled) vibrateShort(context, 30)
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                playArpeggio(listOf(350.0, 420.0, 390.0), noteDurationMs = 40)
            } catch (_: Exception) {}
        }
    }

    fun playClick(context: Context) {
        if (isVibrationEnabled) vibrateShort(context, 20)
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 40)
                toneGen.startTone(ToneGenerator.TONE_PROP_PROMPT, 30)
                toneGen.release()
            } catch (_: Exception) {}
        }
    }

    private fun playSynthesizedTone(frequency: Double, durationMs: Int, volume: Float = 0.5f) {
        val sampleRate = 44100
        val numSamples = (durationMs * sampleRate) / 1000
        val sample = DoubleArray(numSamples)
        val generatedSnd = ByteArray(2 * numSamples)

        for (i in 0 until numSamples) {
            sample[i] = sin(2 * PI * i / (sampleRate / frequency))
        }

        var idx = 0
        for (dVal in sample) {
            val s = (dVal * 32767 * volume).toInt().toShort()
            generatedSnd[idx++] = (s.toInt() and 0x00ff).toByte()
            generatedSnd[idx++] = ((s.toInt() and 0xff00) ushr 8).toByte()
        }

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(generatedSnd.size)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        audioTrack.play()
        Thread.sleep(durationMs.toLong())
        audioTrack.stop()
        audioTrack.release()
    }

    private fun playArpeggio(frequencies: List<Double>, noteDurationMs: Int) {
        val sampleRate = 44100
        val totalSamples = frequencies.size * (noteDurationMs * sampleRate / 1000)
        val generatedSnd = ByteArray(2 * totalSamples)
        var idx = 0

        for (freq in frequencies) {
            val numSamples = (noteDurationMs * sampleRate) / 1000
            for (i in 0 until numSamples) {
                val envelope = 1.0 - (i.toDouble() / numSamples) * 0.3
                val sample = sin(2 * PI * i / (sampleRate / freq)) * envelope
                val s = (sample * 32767 * 0.6f).toInt().toShort()
                generatedSnd[idx++] = (s.toInt() and 0x00ff).toByte()
                generatedSnd[idx++] = ((s.toInt() and 0xff00) ushr 8).toByte()
            }
        }

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(generatedSnd.size)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        audioTrack.play()
        Thread.sleep((frequencies.size * noteDurationMs).toLong())
        audioTrack.stop()
        audioTrack.release()
    }

    private fun getVibrator(context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private fun vibrateShort(context: Context, durationMs: Long) {
        try {
            val vibrator = getVibrator(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    private fun vibrateLong(context: Context, durationMs: Long) {
        try {
            val vibrator = getVibrator(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    private fun vibratePattern(context: Context, pattern: LongArray) {
        try {
            val vibrator = getVibrator(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, -1)
            }
        } catch (_: Exception) {}
    }
}
