package com.leoevg.project246.ui.theme

import android.R.attr.duration
import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.leoevg.project246.R
import com.leoevg.project246.data.Song
import kotlinx.coroutines.delay
import java.util.Random

@Composable
fun PlayerScreen(
    songList: List<Song>,
    initialIndex: Int = 0,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }                 // ядро плеера

    var currentIndex by rememberSaveable { mutableStateOf(initialIndex) }   // какая песня из songList сейчас активна.
    var isShuffle by rememberSaveable { mutableStateOf(false) }
    var isRepeat by rememberSaveable { mutableStateOf(false) }
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var elapsed by rememberSaveable { mutableStateOf(0L) }                  // отслеживание текущего прогресса
    var duration by rememberSaveable { mutableStateOf(0L) }
    var shuffledList by rememberSaveable { mutableStateOf(songList) }
    val waveform = remember { getWaveForm() }
    var waveformProgress by remember { mutableStateOf(0f) }                 // для управления визуализатором аудио волны.

    // Это основная логика для воспроизведения песни.
    LaunchedEffect(currentIndex, isShuffle) {
        val list = if (isShuffle) shuffledList.shuffled() else songList
        val song = list.getOrNull(currentIndex) ?: return@LaunchedEffect
        exoPlayer.setMediaItem(MediaItem.fromUri(song.data))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }
    // для управления жизненным циклом плеера и реакции на его события.
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlay: Boolean) {
                isPlaying = isPlay
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    duration =
                        exoPlayer.duration           //   общая длительность песни (для ползунка перемотки
                }
                if (playbackState == Player.STATE_ENDED) {
                    currentIndex = (currentIndex + 1) % (if (isShuffle) shuffledList.size
                    else songList.size)
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.release()
        }
    }

    // Обновление индикатора прогресса
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
                modifier = Modifier
                    .fillMaxSize()
                    .blur(18.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.4f,
                error = painterResource(id = R.drawable.baseline_music_note_24),
                placeholder = painterResource((R.drawable.baseline_music_note_24))

            )
            Row(Modifier.padding(horizontal = 16.dp, vertical = 48.dp)) {
                IconButton(
                    onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0x30ffffff), shape = CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0x30ffffff), shape = CircleShape)
                ) {
                    Icon(Icons.Default.FavoriteBorder, null, tint = Color.White)
                }
            }



            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = song?.let {
                        ContentUris.withAppendedId(
                            Uri.parse("content://media/external/audio/albumart"),
                            it.albumId
                        )
                    }
                        ?: R.drawable.baseline_music_note_24,
                    contentDescription = null,
                    modifier = Modifier
                        .size(320.dp)
                        .clip(CircleShape)
                        .background(Color(0x33000000), CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.baseline_music_note_24),
                    placeholder = painterResource(R.drawable.baseline_music_note_24)
                )

                Text(
                    song?.title.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Text(
                    song?.artist.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 54.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // repeat button
                IconButton(onClick = {
                    isRepeat = !isRepeat
                    exoPlayer.repeatMode =
                        if (isRepeat) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                }) {
                    Icon(
                        painterResource(R.drawable.outline_repeat_one_24),
                        contentDescription = null,
                        tint = if (isRepeat) Color(0xff9c27b0) else Color.White
                    )
                }

                // skip the previous button
                IconButton(onClick = {
                    val list = if (isShuffle) shuffledList else songList
                    currentIndex = if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
                }) {
                    Icon(
                        painterResource(R.drawable.outline_skip_previous_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                //play-pause button
                IconButton(onClick = {
                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                },
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White, shape = CircleShape)) {
                    Icon(
                        painterResource(if (isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24),
                        contentDescription = null,
                        tint = Color(0xff1a1a1a)
                    )
                }

                // skip next Button
                IconButton(onClick = {
                    val list = if (isShuffle) shuffledList else songList
                    currentIndex = (currentIndex + 1) % list.size
                }) {
                    Icon(
                        painterResource(R.drawable.outline_skip_next_24),
                        contentDescription = null,
                        tint = if (isRepeat) Color(0xff9c27b0) else Color.White
                    )
                }

                // shuffle button
                IconButton(onClick = {
                    isShuffle = !isShuffle
                    shuffledList = if (isShuffle) songList.shuffled() else songList
                }) {
                    Icon(
                        painterResource(R.drawable.outline_shuffle_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }


            }


        }
    }
}


fun getWaveForm(): IntArray {
    val random = Random(System.currentTimeMillis())
    return IntArray(50) { 5 + random.nextInt(50) }
}