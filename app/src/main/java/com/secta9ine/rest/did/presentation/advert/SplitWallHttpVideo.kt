package com.secta9ine.rest.did.presentation.advert

import android.content.Context
import android.graphics.Matrix
import android.net.Uri
import android.view.TextureView
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import kotlin.math.max

@Composable
fun SplitWallHttpVideo(
    url: String,
    screenIndex: Int,          // 0=LEFT, 1=RIGHT (프로토타입: 하드코딩)
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val textureView = remember { TextureView(context) }

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
        }
    }

    DisposableEffect(url, screenIndex) {
        player.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
        player.prepare()

        player.setVideoTextureView(textureView)

        val listener = object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                applyHorizontalSplitTransform(textureView, videoSize, screenIndex)
            }
        }
        player.addListener(listener)

        val layoutListener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val vs = player.videoSize
            if (vs.width > 0 && vs.height > 0) {
                applyHorizontalSplitTransform(textureView, vs, screenIndex)
            }
        }
        textureView.addOnLayoutChangeListener(layoutListener)

        onDispose {
            textureView.removeOnLayoutChangeListener(layoutListener)
            player.removeListener(listener)

            player.release()
        }
    }

    AndroidView(
        factory = { textureView },
        modifier = modifier
    )
}

fun applyHorizontalSplitTransform(
    textureView: TextureView,
    videoSize: VideoSize,
    screenIndex: Int // 0=LEFT, 1=RIGHT
) {
    val vw = textureView.width.toFloat()
    val vh = textureView.height.toFloat()
    if (vw <= 0f || vh <= 0f) return

    val srcW = videoSize.width.toFloat().takeIf { it > 0f } ?: return
    val srcH = videoSize.height.toFloat().takeIf { it > 0f } ?: return

    val scale = max(vw / srcW, vh / srcH)
    val scaledW = srcW * scale
    val scaledH = srcH * scale

    val baseDx = (vw - scaledW) / 2f
    val baseDy = (vh - scaledH) / 2f

    val halfW = scaledW / 2f
    val shift = halfW / 2f

    val dx = when (screenIndex) {
        0 -> baseDx + shift   // LEFT
        1 -> baseDx - shift   // RIGHT
        else -> baseDx        // fallback
    }

    val m = Matrix()
    m.setScale(scale, scale)
    m.postTranslate(dx, baseDy)

    textureView.setTransform(m)
    textureView.invalidate()
}

fun computeSyncedPositionMs(
    context: Context,
    player: ExoPlayer,
    epochStartMs: Long
): Long {
    val nowServerMs = System.currentTimeMillis()

    val duration = player.duration
    if (duration <= 0) return 0L

    val rawPos = nowServerMs - epochStartMs
    val pos = ((rawPos % duration) + duration) % duration
    return pos
}
