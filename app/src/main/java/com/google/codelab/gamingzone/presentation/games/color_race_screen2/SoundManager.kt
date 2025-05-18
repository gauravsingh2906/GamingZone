package com.google.codelab.gamingzone.presentation.games.color_race_screen2


import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.RawRes
import com.google.codelab.gamingzone.R

class SoundManager(context: Context) {
    private val soundPool = SoundPool.Builder().setMaxStreams(5).build()
    private val clickSound = soundPool.load(context, R.raw.click_sound, 1)
    private val winSound = soundPool.load(context, R.raw.win_sound, 1)
    private var backgroundMusic: MediaPlayer? = null

    fun playClickSound() {
        soundPool.play(clickSound, 1f, 1f, 0, 0, 1f)
    }

    fun playWinSound() {
        soundPool.play(winSound, 1f, 1f, 0, 0, 1f)
    }

    fun startBackgroundMusic(context: Context, @RawRes musicRes: Int) {
        backgroundMusic = MediaPlayer.create(context, musicRes)
        backgroundMusic?.isLooping = true
        backgroundMusic?.start()
    }

    fun stopBackgroundMusic() {
        backgroundMusic?.stop()
        backgroundMusic?.release()
        backgroundMusic = null
    }
}
