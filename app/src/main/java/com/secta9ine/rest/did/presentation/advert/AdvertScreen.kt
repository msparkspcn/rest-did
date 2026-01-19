package com.secta9ine.rest.did.presentation.advert

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun AdvertScreen(
    viewModel: AdvertViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val advertList by viewModel.advertList.collectAsState()

    var currentIndex by rememberSaveable { mutableStateOf(0) }


    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    fun playNextAdvert() {
        if (advertList.isEmpty()) return

        currentIndex = (currentIndex + 1) % advertList.size
        val nextUri = advertList[currentIndex].imgPath ?: return

        Log.d("AdvertScreen", "playNextAdvert index=$currentIndex uri=$nextUri")

        exoPlayer.apply {
            clearMediaItems()
            setMediaItem(MediaItem.fromUri(nextUri))
            prepare()
            playWhenReady = true
        }
    }


    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                Log.d("AdvertScreen", "state=$state")

                if (state == Player.STATE_ENDED) {
                    playNextAdvert()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e("AdvertScreen", "error", error)
                playNextAdvert() // 에러 시에도 다음 광고
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    /**
     * 광고 목록이 준비되었을 때 최초 재생
     */
    LaunchedEffect(advertList) {
        if (advertList.isEmpty()) return@LaunchedEffect

        currentIndex = 0
        val uri = advertList[currentIndex].imgPath ?: return@LaunchedEffect

        Log.d("AdvertScreen", "start advert index=0 uri=$uri")

        exoPlayer.apply {
            clearMediaItems()
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
//            volume = 0f //음소거
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
            }
        }
    )
}
