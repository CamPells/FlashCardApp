package cpe81.flashcard.app.components

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun playCorrectSound(context: Context) {
        try {
            mediaPlayer?.release()
            val assetFileDescriptor = context.assets.openFd("correct_answer.mp3")
            mediaPlayer = MediaPlayer().apply {
                setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                prepare()
                start()
            }
            assetFileDescriptor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}