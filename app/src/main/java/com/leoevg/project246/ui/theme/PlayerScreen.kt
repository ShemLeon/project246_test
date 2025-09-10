package com.leoevg.project246.ui.theme

import android.R.attr.duration
import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.Disposable
import coil.request.ImageRequest
import com.leoevg.project246.R
import com.leoevg.project246.data.Song
import com.leoevg.project246.ui.theme.SongListScreen
import kotlinx.coroutines.delay
import java.util.Random
import kotlin.coroutines.EmptyCoroutineContext.get

@Composable
fun PlayerScreen(
    songList: List<Song>,
    initialIndex: Int = 0,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    val currentIndex by rememberSaveable { mutableStateOf(initialIndex) }
    var isShuffle by rememberSaveable { mutableStateOf(false) }
    var currentIndex by rememberSaveable { mutableStateOf(false) }
    var isRepeat by rememberSaveable { mutableStateOf(false) }
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var elapsed by rememberSaveable { mutableStateOf(0L) }
    var shuffledList by rememberSaveable { mutableStateOf(songList) }
    val waveform = remember { get }
    var waveformProgress by remember { mutableStateOf(0f) }


    LaunchedEffect(currentIndex, isShuffle) {
        val list = if (isShuffle) shuffledList.shuffled() else songList
        val song = list.getOrNull(currentIndex) ?: return@LaunchedEffect
        exoPlayer.setMediaItem(MediaItem.fromUri(song.data))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isPlaying = isPlay
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    duration = exoPlayer.duration
                }
                if (playbackState == Player.STATE_ENDED) {
                    currentIndex = (currentIndex + 1) % (if (isShuffle) shuffledList.size
                    else songList.size)
                }
            }

        }
        exoPlayer.addListener(listener)
        OnDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            elapsed = exoPlayer.currentPosition
            waveformProgress = if (duration > 0) elapsed.toFloat() / duration else 0f
            delay(500)
        }
    }

    val song = (if (isShuffle) shuffledList else songList).getOrNull(currentIndex)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xff191c1f),
                        Color(0xff2c2c38)
                    )
                )
            )
    ) {
        song?.let {
            val albumUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                it.albumId
            )
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(albumUri)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().blur(18.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.4f,
                error = painterResource(id = R.drawable.baseline_music_note_24),
                placeholder = painterResource((R.drawable.baseline_music_note_24))

            )


        }
    }
}


fun getWaveForm(): IntArray {
    val random = Random(System.currentTimeMillis())
    return IntArray(50) { 5 + random.nextInt(50) }
}