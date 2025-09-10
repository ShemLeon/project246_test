package com.leoevg.project246.ui.theme

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.leoevg.project246.R
import com.leoevg.project246.data.Song
import com.leoevg.project246.data.getSongs

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SongListScreen(
    onSongClick: (songs: List<Song>, position: Int) -> Unit
) {
    val context = LocalContext.current
    val songsState = remember { mutableStateOf<List<Song>>(emptyList()) }

    // учет разрешения для старых и новых версий андроида
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val permissionState = rememberPermissionState(permission)

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            songsState.value = getSongs(context)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(Modifier.fillMaxSize()) {
            Text(
                "Explorer Artist",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 44.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            if (!permissionState.status.isGranted) {
                Button(
                    onClick = {  permissionState.launchPermissionRequest() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Permission To Musics")
                }
            }

            SongList(
                songs = songsState.value,
                onSongClick = { pos ->
                    onSongClick(songsState.value, pos)
                },
                modifier = Modifier.weight(1f)
            )
        }

    }
}
