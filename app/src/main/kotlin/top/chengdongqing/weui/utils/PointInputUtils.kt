package top.chengdongqing.weui.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.PI
import kotlin.math.abs

suspend fun PointerInputScope.detectTransformGesturesWithManualConsuming(
    panZoomLock: Boolean = false,
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float, consumeChanges: () -> Unit) -> Unit
) {
    awaitEachGesture {
        var rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop
        var lockedToPanZoom = false

        awaitFirstDown(requireUnconsumed = false)
        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.fastAny { it.isConsumed }
            if (!canceled) {
                val zoomChange = event.calculateZoom()
                val rotationChange = event.calculateRotation()
                val panChange = event.calculatePan()

                if (!pastTouchSlop) {
                    zoom *= zoomChange
                    rotation += rotationChange
                    pan += panChange

                    val centroidSize = event.calculateCentroidSize(useCurrent = false)
                    val zoomMotion = abs(1 - zoom) * centroidSize
                    val rotationMotion = abs(rotation * PI.toFloat() * centroidSize / 180f)
                    val panMotion = pan.getDistance()

                    if (zoomMotion > touchSlop ||
                        rotationMotion > touchSlop ||
                        panMotion > touchSlop
                    ) {
                        pastTouchSlop = true
                        lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                    }
                }

                if (pastTouchSlop) {
                    val centroid = event.calculateCentroid(useCurrent = false)
                    val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                    if (effectiveRotation != 0f ||
                        zoomChange != 1f ||
                        panChange != Offset.Zero
                    ) {
                        onGesture(
                            centroid,
                            panChange,
                            zoomChange,
                            effectiveRotation
                        ) {
                            event.changes.fastForEach {
                                if (it.positionChanged()) {
                                    it.consume()
                                }
                            }
                        }
                    }
                }
            }
        } while (!canceled && event.changes.fastAny { it.pressed })
    }
}

suspend fun PointerInputScope.detectDragGesturesAfterLongPressWithoutConsume(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit
) {
    awaitEachGesture {
        try {
            val down = awaitFirstDown(requireUnconsumed = false)
            val drag = awaitLongPressOrCancellation(down.id)
            if (drag != null) {
                onDragStart.invoke(drag.position)

                if (
                    drag(drag.id) {
                        onDrag(it, it.positionChange())
                    }
                ) {
                    // consume up if we quit drag gracefully with the up
                    currentEvent.changes.fastForEach {
                        if (it.changedToUp()) it.consume()
                    }
                    onDragEnd()
                } else {
                    onDragCancel()
                }
            }
        } catch (c: CancellationException) {
            onDragCancel()
            throw c
        }
    }
}