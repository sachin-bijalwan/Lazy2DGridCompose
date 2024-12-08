package com.zeel.aparecium.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeel.aparecium.myapplication.ui.theme.MyApplicationTheme
import com.zeel.aparecium.myapplication.ui.theme.Test
import kotlin.math.max

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box(modifier) {
        Test()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
/**
@Composable
fun TwoDimensionScrollableGrid(
    totalHeight: Dp,
    colCount: Int,
    rowCount: Int,
    itemComposable: @Composable (Int,Int) -> Unit
) {
    val horizontalScrollState = rememberScrollState()
    LazyRow (modifier = Modifier.
    height(totalHeight)) {
         items (rowCount) {row ->
            val childState = rememberLazyListState()
            LazyColumn(state = childState, userScrollEnabled = false) {
                items(colCount) { col: Int ->
                    itemComposable.invoke(row,col)
                    LogCompositions("zimati","$row $col and ")
                }
            }
        }
    }
}
@Composable
fun ExcelSheet(data: ImmutableList) {
    val textMeasurer = rememberTextMeasurer()
    val padding = with(LocalDensity.current) {
        28.dp.toPx().toInt()
    }
    val height =
        remember(data) {
            textMeasurer.measure(data.data[0][0]).size.height+ padding
        }
    TwoDimensionScrollableGrid(
        totalHeight = with(LocalDensity.current) { (height*data.data.size).toDp() },
        colCount = data.data.size,
        rowCount = data.data[0].size
    ) { row, col ->
        val height = with(LocalDensity.current) {height.toDp()}
        Text(data.data[col][row],Modifier.height(height).width(50.dp), style = TextStyle(platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )))
    }
   // with(LocalDensity.current) { widthInPixels.toDp() }
}
@Composable
fun Test() {
    val data = MutableList(50) { col ->
        MutableList(20) {
            "abc$it/$col"
        }
    }
    ExcelSheet(ImmutableList(data))
}
class RecompositionCounter(var value: Int)
@Immutable
data class ImmutableList(val data: List<List<String>>)

@Composable
fun LogCompositions(tag: String, msg: String) {
    if (true) {
        val recompositionCounter = remember { RecompositionCounter(0) }

        Log.d(tag, "$msg ${recompositionCounter.value} $currentRecomposeScope")
        recompositionCounter.value++
    }
}
/**
 * r: row, c: col
 * 0..r
 * .
 * .
 * c
 * outer loop is c and inner loop is r
 *
 * first index is col then row
 */