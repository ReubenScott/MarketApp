package com.kindustry.market.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.kindustry.market.ui.screen.LocalPaddingValues
import com.kindustry.market.viewmodel.EquityInfo
import com.kindustry.market.viewmodel.MainViewModel
import kotlinx.coroutines.launch


// 使用示例
@Composable
fun MyFavorite(equitys: List<EquityInfo>) {
    val headerList = listOf(
        listOf("コード", "銘柄名", "業種","dividendYield", "debtAssetRatio", "per", "pbr")
    )
    val dataList = equitys.map {
        listOf(it.symbol, it.name, it.sector, it.dividendYield?.toString(), it.debtAssetRatio?.toString(), it.per?.toString(), it.pbr?.toString())
    }
//    ScrollableTable(headerList + dataList)
}

@Composable
fun CombinedList(outerItems: List<List<String?>>) { // 添加水平滚动
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(outerItems) { innerItems ->
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(innerItems) { item ->
                    Spacer(modifier = Modifier.wrapContentWidth()) // 添加一个 Spacer 来分隔字段名和内容
                    Text(
                        text = item?:"",
                        modifier = Modifier.wrapContentWidth()
                    )

                }
            }
        }
    }
}

@Composable
fun LazyList() {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items( 100){
            Text(text = "Item #$it", style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
fun ScrollingList(){
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutlineScope =  rememberCoroutineScope()

    Column {
        Row() {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutlineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                }
            ) {
                Text(text = "Scroll to the top")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutlineScope.launch {
                        scrollState.animateScrollToItem(listSize -1 )
                    }
                }
            ) {
                Text(text = "Scroll to the end")
            }
        }

        LazyColumn(state = scrollState) {
            items( 50){
                ImageListItem(index = it)
            }
        }
    }

}

@Composable
fun ImageListItem(index: Int){
    Row (verticalAlignment = Alignment.CenterVertically){
        Image(
            painter = rememberImagePainter(
                data = "https://upload.wikimedia.org/wikipedia/commons/e/e6/Android_vector.jpg"
            ),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}


@Composable
fun ScrollableTable(
    viewModel: MainViewModel,
//    equitys: List<EquityInfo>,
    onSubmit: (String, String) -> Unit
){
//    val coroutlineScope =  rememberCoroutineScope()

    val position by viewModel.scrollPosition.collectAsState()
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = position)


    /*
    var isAscending by remember { mutableStateOf(true) }
    var sortColumn by remember { mutableStateOf("") }

    var sortedEquitys = remember(equitys, sortColumn, isAscending) {
        val sortedList = when (sortColumn) {
            "name" -> if (isAscending) equitys.sortedBy { it.name } else equitys.sortedByDescending { it.name }
            "sector" -> if (isAscending) equitys.sortedBy { it.sector } else equitys.sortedByDescending { it.sector }
            "dividendYield" -> if (isAscending) equitys.sortedBy { it.dividendYield } else equitys.sortedByDescending { it.dividendYield }
            "debtAssetRatio" -> if (isAscending) equitys.sortedBy { it.debtAssetRatio } else equitys.sortedByDescending { it.debtAssetRatio }
            "per" -> if (isAscending) equitys.sortedBy { it.per } else equitys.sortedByDescending { it.per }
            "pbr" -> if (isAscending) equitys.sortedBy { it.pbr } else equitys.sortedByDescending { it.pbr }
            else -> if (isAscending) equitys.sortedBy { it.symbol } else equitys.sortedByDescending { it.symbol } // 默认按 symbol 排序
        }
        sortedList.toMutableStateList()
    }*/

    val isAscending by viewModel.isAscendingFlow.collectAsState()
    val sortColumn by viewModel.sortColumnFlow.collectAsState()
    val sortedEquitys by viewModel.equityListFlow.collectAsState()

    LaunchedEffect(sortColumn, isAscending,position) {
        viewModel.sortEquityInfo(sortColumn, isAscending)
        lazyListState.scrollToItem(position)
    }

    DisposableEffect(lazyListState) {
        onDispose {
            viewModel.setScrollPosition(lazyListState.firstVisibleItemIndex)
        }
    }

//    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Row(
        modifier = Modifier.fillMaxSize().horizontalScroll(horizontalScrollState)
    ) {
    Column(
        modifier = Modifier.weight(1f).padding(LocalPaddingValues.current),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start // 将 Row 的内容从左侧开始排列
            ) {
                ClickableText(
                    text = buildAnnotatedString { append("コード") },
                    style = MaterialTheme.typography.h6.copy(color = Color.White), // 使用主题中的标题样式，并设置颜色为白色
                    modifier = Modifier.weight(1.5f),// 让字段名占据整个宽度
                    onClick = {
//                        isAscending = !isAscending
//                        sortColumn = "symbol"
                        viewModel.updateIsAscending(!isAscending)
                        viewModel.updateSortColumn("symbol")
                    }
                )
                Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                ClickableText(
                    text = buildAnnotatedString { append("銘柄名") },
                    style = MaterialTheme.typography.h6.copy(color = Color.White), // 使用主题中的标题样式，并设置颜色为白色
                    modifier = Modifier.weight(4f), // 让字段名占据整个宽度
                    onClick = {
//                        isAscending = !isAscending
//                        sortColumn = "name"
                        viewModel.updateIsAscending(!isAscending)
                        viewModel.updateSortColumn("name")
                    }
                )
                Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                ClickableText(
                    text = buildAnnotatedString { append("業種") },
                    style = MaterialTheme.typography.h6.copy(color = Color.White), // 使用主题中的标题样式，并设置颜色为白色
                    modifier = Modifier.weight(2f),// 让字段名占据整个宽度
                    onClick = {
//                        isAscending = !isAscending
//                        sortColumn = "sector"
                        viewModel.updateIsAscending(!isAscending)
                        viewModel.updateSortColumn("sector")
                    }
                )
                Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                ClickableText(
                    text = buildAnnotatedString { append("股息") },
                    style = MaterialTheme.typography.h6.copy(
                        color = Color.White,
                        textAlign = TextAlign.End
                    ), // 使用主题中的标题样式，并设置颜色为白色
                    modifier = Modifier.weight(1.5f), // 让字段名占据整个宽度
                    onClick = {
//                        isAscending = !isAscending
//                        sortColumn = "dividendYield"
                        viewModel.updateIsAscending(!isAscending)
                        viewModel.updateSortColumn("dividendYield")
                    }
                )
                Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                ClickableText(
                    text = buildAnnotatedString { append("負債") },
                    style = MaterialTheme.typography.h6.copy(
                        color = Color.White,
                        textAlign = TextAlign.End
                    ), // 使用主题中的标题样式，并设置颜色为白色
                    modifier = Modifier.weight(1.5f), // 让字段名占据整个宽度
                    onClick = {
//                        isAscending = !isAscending
//                        sortColumn = "debtAssetRatio"
                        viewModel.updateIsAscending(!isAscending)
                        viewModel.updateSortColumn("debtAssetRatio")
                    }
                )
                Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                ClickableText(
                    text = buildAnnotatedString { append("PER") },  // 株価収益率
                    style = MaterialTheme.typography.h6.copy(
                        color = Color.White,
                        textAlign = TextAlign.End
                    ), // 使用主题中的标题样式，并设置颜色为白色
                    modifier = Modifier.weight(1.5f), // 让字段名占据整个宽度
                    onClick = {
//                        isAscending = !isAscending
//                        sortColumn = "per"
                        viewModel.updateIsAscending(!isAscending)
                        viewModel.updateSortColumn("per")
                    }
                )
                Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                ClickableText(
                    text = buildAnnotatedString { append("PBR") },  // 株価純資産倍率
                    style = MaterialTheme.typography.h6.copy(
                        color = Color.White,
                        textAlign = TextAlign.End
                    ), // 使用主题中的标题样式，并设置颜色为白色
                    modifier = Modifier.weight(1f), // 让字段名占据整个宽度
                    onClick = {
//                        isAscending = !isAscending
//                        sortColumn = "pbr"
                        viewModel.updateIsAscending(!isAscending)
                        viewModel.updateSortColumn("pbr")
                    }
                )
            }
        }


        // 显示股票列表 LazyColumn  itemsIndexed LazyVerticalGrid
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyListState
        ) {
            itemsIndexed(sortedEquitys) { index, item ->
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
                            if (index % 2 == 0) Color.White else Color.LightGray
                        )
                        .clickable {
                            isExpanded = !isExpanded
                            // 在这里处理点击事件
                            onSubmit(item.symbol, item.name)
                        }
                ) {
                    Text(
                        text = item.symbol,
                        color = surfaceColor,
                        modifier = Modifier.weight(1.5f),
                        textAlign = TextAlign.Start // 左对齐
                    )
                    Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                    Text(
                        text = item.name, // 当 name 为 null 时，显示默认值
                        color = surfaceColor,
//                        text = item.name?.take(10)?.plus("...") ?: "", // 当 name 为 null 时，显示默认值
                        modifier = Modifier.weight(4f),
                        textAlign = TextAlign.Start // 左对齐
                    )
                    Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                    Text(
                        text = item.sector, // 当 name 为 null 时，显示默认值
                        color = surfaceColor,
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Start // 左对齐
                    )
                    Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                    Text(
                        text = item.dividendYield?.let { "${String.format("%.2f", it)}%" }
                            ?: "", // 格式化 小数点后显示2位
                        color = surfaceColor,
                        modifier = Modifier.weight(1.5f),
                        textAlign = TextAlign.End // 右对齐
                    )
                    Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                    Text(
                        text = item.debtAssetRatio?.let { "${String.format("%.2f", it)}%" }
                            ?: "", //  添加 %
                        color = surfaceColor,
                        modifier = Modifier.weight(1.5f),
                        textAlign = TextAlign.End // 右对齐
                    )
                    Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                    Text(
                        text = item.per?.let {
                            String.format("%.2f", it) // 格式化 小数点后显示2位
                        } ?: "",  // 格式化为两位小数
                        color = surfaceColor,
                        modifier = Modifier.weight(1.5f),
                        textAlign = TextAlign.End // 右对齐
                    )
                    Spacer(modifier = Modifier.weight(1f)) // 添加一个 Spacer 来分隔字段名和内容
                    Text(
                        text = item.pbr?.let {
                            String.format("%.2f", it)  // 格式化 小数点后显示2位
                        } ?: "",  // 格式化为两位小数
                        color = surfaceColor,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End // 右对齐
                    )
                }
            }
        }

    }

    }
}


@Composable
fun HeaderItem(label: String, viewModel: MainViewModel, isAscending: Boolean) {
    ClickableText(
        text = buildAnnotatedString {append(label) },  // 株価純資産倍率
        style = MaterialTheme.typography.h6.copy(color = Color.White, textAlign = TextAlign.End), // 使用主题中的标题样式，并设置颜色为白色
//        modifier = Modifier.weight(1f), // 让字段名占据整个宽度
        onClick = {
            viewModel.updateIsAscending(!isAscending)
            viewModel.updateSortColumn("pbr")
        }
    )
}


@Composable
fun ColumnItem(value: Any?, unit: String? = "") {
    Text(
        text = value?.let { "${it}$unit" } ?: "",
//        color = surfaceColor,
//        modifier = Modifier.weight(1f),
        textAlign = TextAlign.End // 右对齐
    )
}


