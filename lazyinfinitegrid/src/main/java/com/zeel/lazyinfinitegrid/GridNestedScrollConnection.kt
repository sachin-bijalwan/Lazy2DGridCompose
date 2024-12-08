package com.zeel.lazyinfinitegrid

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import kotlin.math.abs

class GridNestedScrollConnection(
    private val currentColumnScroll: MutableState<Float>,
    private val currentHorizontalScroll: MutableState<Float>,
    private val maxVerticalScrollThatCanHappen: Float,
    private val maxHorizontalScrollThatCanHappen: Float
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val newVerticalScroll =
            (currentColumnScroll.value + (-1 * available.y)).coerceAtMost(
                maxVerticalScrollThatCanHappen
            )
                .coerceAtLeast(0f)
        val newHorizontalScroll =
            (currentHorizontalScroll.value + (-1 * available.x)).coerceAtMost(
                maxHorizontalScrollThatCanHappen
            )
                .coerceAtLeast(0f)

        if (abs(available.y) > 0f && abs(available.x) < abs(available.y)
            && newVerticalScroll != currentColumnScroll.value
        ) {
            currentColumnScroll.value = newVerticalScroll
            return Offset(available.x, available.y)
        } else if (abs(available.x) > 0f &&
            abs(available.y) < abs(available.x) &&
            newHorizontalScroll != currentHorizontalScroll.value
        ) {

            currentHorizontalScroll.value = newHorizontalScroll
            return Offset(available.x, available.y)
        }
        return super.onPreScroll(available, source)
    }
}