package com.kindustry.market.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun SideDrawer(
    exchanges: List<String>,
    sectors: List<String>,
    queryCondition: List<Any>,
    onSubmit: (List<Any>) -> Unit
) {
    var exchange by remember { mutableStateOf(queryCondition.getOrNull(0) ?: "") }
    var sector by remember { mutableStateOf(queryCondition.getOrNull(1) ?: "") }
    var yearChangeRange by remember { mutableStateOf(queryCondition.getOrNull(2) ?: "") }
    var movingAverageRange by remember { mutableStateOf(queryCondition.getOrNull(3) ?: "") }
    var debtAssetRange by remember { mutableStateOf(queryCondition.getOrNull(4) ?: "") }
    var perRange by remember { mutableStateOf(queryCondition.getOrNull(5) ?: "") }
    var pbrRange by remember { mutableStateOf(queryCondition.getOrNull(6) ?: "") }
    var dividendYieldRange by remember { mutableStateOf(queryCondition.getOrNull(7) ?: "") }
    var priceRange by remember { mutableStateOf(queryCondition.getOrNull(8) ?: "") }

    var isBoxVisible by remember { mutableStateOf(true) }
    var boxBounds by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(isBoxVisible){
        if(!isBoxVisible){
            onSubmit(listOf())
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (isBoxVisible && !boxBounds.contains(offset)) {
                        isBoxVisible = false
                    }
                }
            }
    ) {
            val box = createRef()
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // 宽度为屏幕的比例
                    .background(Color.White.copy(alpha = 0.8f))
                    .constrainAs(box) {
                        start.linkTo(parent.start) // 将 Box 的左侧与父布局的左侧对齐
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .onGloballyPositioned { layoutResult ->
                        boxBounds = layoutResult.boundsInRoot()
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
//                    .width(300.dp)
//                    .height(200.dp)
//                    .background(Color.White.copy(alpha = 0.8f))
//                    .align(Alignment.CenterStart) // 对齐到左侧
                ) {
                    DropdownMenuWithLabel(
                        label = "市場区分 ： ",
                        items = exchanges,
                        onSelectionChanged = {
                            exchange = exchanges[it]
                        },
                        defaultIndex = run {
                             exchanges.indexOf(exchange).takeIf { it != -1 } ?: 0
                        }
                    )

                    DropdownMenuWithLabel(
                        label = "業種 ： ",
                        items = sectors,
                        onSelectionChanged = {
                            sector = sectors[it]
                        },
                        defaultIndex = run {
                            sectors.indexOf(sector).takeIf { it != -1 } ?: 0
                        }
                    )

                    // 上昇率
                    val yearChangeItems = listOf(
                        DropMenuItemData("", ""),
                        DropMenuItemData(",-30", "～-30%"),
                        DropMenuItemData("-30,-10", "-30%～-10%"),
                        DropMenuItemData("-10,0", "-10%～0%"),
                        DropMenuItemData("0,10", "0%～10%"),
                        DropMenuItemData("10,30", "10%～30%"),
                        DropMenuItemData("30,100", "30%～100%"),
                        DropMenuItemData("100,", "100%～"),
                    )
                    DropdownMenuWithLabel(
                        label = "上昇率 ： ",
                        items = yearChangeItems.map{ it.title },
                        onSelectionChanged = {  i ->
                            yearChangeRange = yearChangeItems.map{it.key}[i]
                        },
                        defaultIndex = run {
                            yearChangeItems.indexOfFirst { it.key == yearChangeRange }.takeIf { it != -1 } ?: 0
                        }
                    )

                    // 乖離率
                    val movingAverageItems = listOf(
                        DropMenuItemData("", ""),
                        DropMenuItemData(",-30", "～-30%"),
                        DropMenuItemData("-30,-10", "-30%～-10%"),
                        DropMenuItemData("-10,0", "-10%～0%"),
                        DropMenuItemData("0,10", "0%～10%"),
                        DropMenuItemData("10,30", "10%～30%"),
                        DropMenuItemData("30,100", "30%～100%"),
                        DropMenuItemData("100,", "100%～"),
                    )
                    DropdownMenuWithLabel(
                        label = "乖離率 ： ",
                        items = movingAverageItems.map{ it.title },
                        onSelectionChanged = {  i ->
                            movingAverageRange = movingAverageItems.map{it.key}[i]
                        },
                        defaultIndex = run {
                            movingAverageItems.indexOfFirst { it.key == movingAverageRange }.takeIf { it != -1 } ?: 0
                        }
                    )

                    //  負債比率
                    val debtAssetItems = listOf(
                        DropMenuItemData("", ""),
                        DropMenuItemData(",0.3", "Low (0%～30%)"),
                        DropMenuItemData("0.3,0.6", "Medium (30%～60%)"),
                        DropMenuItemData("0.6,0.9", "High (60%～90%)"),
                        DropMenuItemData("0.9,", "Very High (90%～)"),
                    )
                    DropdownMenuWithLabel(
                        label = "負債比率 ： ",
                        items = debtAssetItems.map{ it.title },
                        onSelectionChanged = {  i ->
                            debtAssetRange = debtAssetItems.map{it.key}[i]
                        },
                        defaultIndex = run {
                            debtAssetItems.indexOfFirst { it.key == debtAssetRange }.takeIf { it != -1 } ?: 0
                        }
                    )

                    //  株価収益率 PER
                    val perItems = listOf(
                        DropMenuItemData("", ""),
                        DropMenuItemData(",0", "Unprofitable (<0x)"),
                        DropMenuItemData("0,20", "Low (0x to <20x)"),
                        DropMenuItemData("20,30", "Medium (20x to <30x)"),
                        DropMenuItemData("30,", "High (>30x)"),
                    )
                    DropdownMenuWithLabel(
                        label = "株価収益率 ： ",
                        items = perItems.map{ it.title },
                        onSelectionChanged = {  i ->
                            perRange = perItems.map{it.key}[i]
                        },
                        defaultIndex = run {
                            perItems.indexOfFirst { it.key == perRange }.takeIf { it != -1 } ?: 0
                        }
                    )

                    //  株価純資産倍率 PBR
                    val pbrItems = listOf(
                        DropMenuItemData("", ""),
                        DropMenuItemData(",1", "0～1"),
                        DropMenuItemData(",2", "0～2"),
                        DropMenuItemData("2,", "2～"),
                    )
                    DropdownMenuWithLabel(
                        label = "株価純資産倍率 ： ",
                        items = pbrItems.map{ it.title },
                        onSelectionChanged = {  i ->
                            pbrRange = pbrItems.map{it.key}[i]
                        },
                        defaultIndex = run {
                            pbrItems.indexOfFirst { it.key == pbrRange }.takeIf { it != -1 } ?: 0
                        }
                    )

                    //  配当利回り
                    val dividendYieldItems = listOf(
                        DropMenuItemData("", ""),
                        DropMenuItemData(",0", "No Dividend"),
                        DropMenuItemData("0,", "Dividend Payer (>0%)"),
                        DropMenuItemData("5,", "High Yield (>5%)"),
                        DropMenuItemData("10,", "Very High Yield (>10%)"),
                    )
                    DropdownMenuWithLabel(
                        label = "配当利回り ： ",
                        items = dividendYieldItems.map{ it.title },
                        onSelectionChanged = {  i ->
                            dividendYieldRange = dividendYieldItems.map{it.key}[i]
                        },
                        defaultIndex = run {
                            dividendYieldItems.indexOfFirst { it.key == dividendYieldRange }.takeIf { it != -1 } ?: 0
                        }
                    )

                    //  株価
                    val priceItems = listOf(
                        DropMenuItemData("", ""),
                        DropMenuItemData(",200", "Very Low (<200)"),
                        DropMenuItemData("200,500", "Low (200～500)"),
                        DropMenuItemData("500,1000", "Mid-Low (500～1000)"),
                        DropMenuItemData("1000,2000", "Mid (1000～2000)"),
                        DropMenuItemData("2000,4000", "Mid-High (2000～4000)"),
                        DropMenuItemData("4000,8000", "High (4000～8000)"),
                        DropMenuItemData("8000,", "Very High (>8000)"),
                    )
                    DropdownMenuWithLabel(
                        label = "株価 ： ",
                        items = priceItems.map{ it.title },
                        onSelectionChanged = {  i ->
                            priceRange = priceItems.map{it.key}[i]
                        },
                        defaultIndex = run {
                            priceItems.indexOfFirst { it.key == priceRange }.takeIf { it != -1 } ?: 0
                        }
                    )

                    BasicSlider()

                    Spacer(modifier = Modifier.width(16.dp))

                    Row {
                        Button(onClick = {
                            exchange = ""
                            sector = ""
                            yearChangeRange = ""
                            movingAverageRange = ""
                            debtAssetRange = ""
                            perRange = ""
                            pbrRange = ""
                            dividendYieldRange = ""
                            priceRange = ""
                        }) {
                            Text("初期化")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(onClick = {
                            onSubmit(listOf(
                                exchange, sector, yearChangeRange, movingAverageRange,
                                debtAssetRange, perRange, pbrRange, dividendYieldRange, priceRange
                              )
                            )
                        }) {
                            Text("検索")
                        }
                    }

                }
//            }

        }
    }
}


@Composable
fun BasicSlider() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Text("当前值：${sliderPosition}") // 显示当前值
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it }
    )
}

data class DropMenuItemData(val key: String, val title: String)

@Composable
fun DropdownMenuWithLabel(
    label: String,
    items: List<String>,
    onSelectionChanged: (Int) -> Unit,
    defaultIndex: Int = 0,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(defaultIndex.coerceIn(0, items.lastIndex)) }

    // 使用 LaunchedEffect 监听 defaultIndex 的变化并更新 selectedIndex
    LaunchedEffect(defaultIndex) {
        selectedIndex = defaultIndex.coerceIn(0, items.lastIndex)
    }

    Row() {
        TextButton(
            onClick = { expanded = !expanded }
        ) {
            Text(text = label)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = if (selectedIndex >= 0) items[selectedIndex] else "")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onSelectionChanged(index)
                    }
                ) {
                    Column {
                        Text(text = item, style = MaterialTheme.typography.caption) // 调整描述的样式
                    }
                }
            }
        }
    }
}
