package com.zeel.lazyinfinitegrid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@Composable
fun LazyInfiniteGrid(
    colCount: Int,
    rowCount: Int,
    heightInPixels: Float,
    maxVerticalScrollThatCanHappen: Float,
    maxHorizontalScrollThatCanHappen: Float,
    modifier: Modifier = Modifier,
    rowHeader: @Composable (Int) -> Unit,
    columnHeader: @Composable (Int) -> Unit,
    itemComposable: @Composable (Int, Int) -> Unit,
) {
    val columnScrollMap = remember {
        mutableStateMapOf<Int, Float>()
    }
    val currentColumnScroll = remember {
        mutableStateOf(0f)
    }
    val currentHorizontalScroll = remember {
        mutableStateOf(0f)
    }
    val nestedScrollConnection = remember(maxVerticalScrollThatCanHappen,maxHorizontalScrollThatCanHappen) {
        GridNestedScrollConnection(
            currentColumnScroll,
            currentHorizontalScroll,
            maxVerticalScrollThatCanHappen,
            maxHorizontalScrollThatCanHappen
        )
    }
    Column(modifier.nestedScroll(nestedScrollConnection)) {
        /**
         * header row and it's listeners
         */
        val headerRowState = rememberLazyListState()
        ListenScroll(
            currentHorizontalScroll, headerRowState,
        )
        LazyRow(state = headerRowState) {
            items(rowCount + 1) {
                columnHeader.invoke(it)
            }
        }
        /**
         * Rest of UI
         */
        Row {
            /**
             * Header column
             */
            val headerColumnState = rememberLazyListState()
            ListenScroll(
                currentColumnScroll, headerColumnState,
            )
            LazyColumn(state = headerColumnState) {
                items(colCount) {
                    rowHeader.invoke(it)
                }
            }

            /**
             * Grid data
             */
            val rowState = rememberLazyListState()
            ListenScroll(
                currentHorizontalScroll,
                rowState
            )
            LazyRow(
                modifier = Modifier.fillMaxHeight(),
                state = rowState
            ) {
                items(rowCount) { row ->
                    val columnScrollState = remember {
                        columnScrollMap[row] = currentColumnScroll.value
                        val scroll = currentColumnScroll.value
                        LazyListState(
                            (scroll / heightInPixels).toInt(),
                            (scroll % heightInPixels).toInt()
                        )
                    }
                    LazyColumn(state = columnScrollState) {
                        items(colCount) { col: Int ->
                            itemComposable.invoke(row, col)
                        }
                    }
                    ListenScroll(currentColumnScroll,
                        columnScrollState,
                        { columnScrollMap[row] ?: 0f }
                    ) {
                        columnScrollMap[row] = it
                    }
                }
            }
        }
    }
}

@Composable
fun ListenScroll(
    currentScroll: MutableState<Float>,
    childState: LazyListState,
    childStateScroll: MutableState<Float> = mutableStateOf(0f)
) {
    ListenScroll(
        currentScroll,
        childState,
        {
            childStateScroll.value
        }
    ) {
        childStateScroll.value = it
    }
}

@Composable
fun ListenScroll(
    currentScroll: MutableState<Float>,
    childState: LazyListState,
    childStateScroll: () -> Float,
    childStateScrollUpdate: (Float) -> Unit
) {
    LaunchedEffect(currentScroll.value) {
        val scrollRequired = currentScroll.value - (childStateScroll.invoke())
        childStateScrollUpdate.invoke(currentScroll.value)
        val consumed = childState.dispatchRawDelta(scrollRequired)
        if (consumed != scrollRequired) {
            childStateScrollUpdate.invoke(childStateScroll.invoke() - (scrollRequired - consumed))
        }
    }
}