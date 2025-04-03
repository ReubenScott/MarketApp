package com.kindustry.market.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kindustry.market.ui.screen.LocalPaddingValues
import com.kindustry.market.viewmodel.MainViewModel


@Composable
fun EquityList (
    viewModel: MainViewModel,
    onSubmit: (String, String) -> Unit
) {
//    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val isAscending by viewModel.isAscendingFlow.collectAsState()
    val sortColumn by viewModel.sortColumnFlow.collectAsState()
    val sortedEquities by viewModel.equityListFlow.collectAsState()
    val position by viewModel.scrollPosition.collectAsState()
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = position)

    LaunchedEffect(sortColumn, isAscending, position) {
        viewModel.sortEquityInfo(sortColumn, isAscending)
        lazyListState.scrollToItem(position)
    }

    DisposableEffect(lazyListState) {
        onDispose {
            viewModel.setScrollPosition(lazyListState.firstVisibleItemIndex)
        }
    }

    // Define a map with immutable keys and values
    val headerMap: Map<String, String> = linkedMapOf(
        "コード" to "symbol",
        "銘柄名" to "name",
        "市場区分" to "exchange",
        "上場日" to "listingDate",
        "業種" to "sector",
        "上昇率" to "yearChangeRatio",
        "乖離率" to "movingAverageRatio",
        "負債率" to "debtAssetRatio",
        "PER" to "per",
        "PBR" to "pbr",
        "股息" to "dividendYield",
        "時価総額" to "marketCap"
    )

    val headerList: List<String> = headerMap.keys.toList()
//    val headerList = listOf("コード", "銘柄名", "業種","股息", "負債率", "PER", "PBR", "上昇率", "乖離率")
    val weightList = listOf(80, 150, 130, 90, 150, 80, 80, 80, 80, 80, 80, 100)

//  text = item.name?.take(10)?.plus("...") ?: "", // 当 name 为 null 时，显示默认值
    val dataList =  sortedEquities.map { it ->
        listOf(
            it.symbol,
            it.name,
            it.exchange,
            it.listingDate,
            it.sector,
            it.yearChangeRatio?.let { "${String.format("%.2f", it)}%" },
            it.movingAverageRatio?.let { "${String.format("%.2f", it)}%" },
            it.debtAssetRatio?.let { "${String.format("%.2f", it)}%" },
            it.per?.let { String.format("%.2f", it) },
            it.pbr?.let { String.format("%.2f", it) },
            it.dividendYield?.let { "${String.format("%.2f", it)}%" },
            it.marketCap?.let { "${String.format("%.1f", it)}億" }
        )
    }

    Row(
        modifier = Modifier.horizontalScroll(horizontalScrollState)
    ) {
        Column(
            modifier = Modifier.padding(LocalPaddingValues.current)
        ) {

            // 固定字段名
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .padding(horizontal = 0.dp)
                    .padding(vertical = 0.dp)
//                .offset(y = (-scrollState.value).dp) // 设置偏移量
            ) {
                Row(
                    modifier = Modifier.background(MaterialTheme.colors.primary)
                ) {
                    headerList.forEachIndexed { index, cell ->
                        ClickableText(
                            text = buildAnnotatedString { append(cell) },
                            style = MaterialTheme.typography.h6.copy(
                                color = Color.White,
                                textAlign = if (index > 2) {
                                    TextAlign.End // 右对齐
                                } else {
                                    TextAlign.Start // 左对齐
                                }
                            ), // 使用主题中的标题样式，并设置颜色为白色
                            modifier = Modifier.width(weightList[index].dp), // 设置固定宽度
//                        modifier = Modifier.background(MaterialTheme.colors.primary).weight(1.5f),// 让字段名占据整个宽度
                            onClick = {
                                viewModel.updateIsAscending(!isAscending)
                                viewModel.updateSortColumn(headerMap[cell] ?: "")
                            }
                        )

                        if (index < headerList.size - 1) {
                            Spacer(modifier = Modifier.width(1.dp)) // 列之间的间距
                        }
                    }
                }
            }


            // 显示股票列表 LazyColumn  itemsIndexed LazyVerticalGrid
            LazyColumn(
                modifier = Modifier.fillMaxSize(), // .padding(LocalPaddingValues.current)
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                state = lazyListState
            ) {
                // 数据行
                itemsIndexed(dataList) { rowIndex, row ->
                    var isExpanded by remember {
                        mutableStateOf(false)
                    }
                    val surfaceColor: Color by animateColorAsState(
                        if (isExpanded) Color.Blue else Color.Black
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .background(
                                if (rowIndex % 2 == 0) Color.White else Color.LightGray
                            )
                            .clickable {
                                isExpanded = !isExpanded
                                // 在这里处理点击事件 symbol, name
                                onSubmit(row[0] ?: "", row[1] ?: "")
                            }
                    ) {
                        row.forEachIndexed { columnIndex, cell ->
                            Text(
                                text = cell ?: "",
                                color = surfaceColor,
                                modifier = Modifier.width(weightList[columnIndex].dp), // 设置固定宽度
                                textAlign = if (columnIndex > 2) {
                                    TextAlign.End // 右对齐
                                } else {
                                    TextAlign.Start // 左对齐
                                },
                                maxLines = 1, // 设置最大行数为 1，即不换行
                                overflow = TextOverflow.Ellipsis // 设置文本溢出时的处理方式为省略号
                            )

                            if (columnIndex < row.size - 1) {
                                Spacer(modifier = Modifier.width(1.dp)) // 列之间的间距
                            }
                        }
                    }

                    if (rowIndex < dataList.size - 1) {
                        Spacer(modifier = Modifier.padding(1.dp)) // 行之间的间距
                    }
                }
            }

        }


    }
}