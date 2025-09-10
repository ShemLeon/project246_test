package com.leoevg.project246.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.media3.exoplayer.ExoPlayer
import com.leoevg.project246.data.Song
import com.leoevg.project246.ui.theme.SongListScreen

@Composable
fun PlayerScreen(
    songList: List<Song>,
    initialIndex: Int=0,
    onBack: ()->Unit
){
    val context = LocalContext.current
    val exoPlayer = remember{ExoPlayer.Builder(context).build()}
    val currentIndex by rememberSaveable { mutableStateOf(initialIndex) }

}