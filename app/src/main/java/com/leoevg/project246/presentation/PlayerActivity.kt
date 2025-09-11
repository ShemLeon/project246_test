package com.leoevg.project246.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.leoevg.project246.R
import com.leoevg.project246.data.Song
import com.leoevg.project246.ui.theme.PlayerScreen
import com.leoevg.project246.ui.theme.SongListScreen

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val mySongList = intent.getParcelableArrayListExtra("songList") ?: emptyList<Song>()
        val initialIndex = intent.getIntExtra("initialIndex", 0)
        setContent {
            PlayerScreen(
                songList = mySongList,
                initialIndex = initialIndex,
                onBack = { finish() }
            )
        }
    }
}
