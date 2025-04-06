package com.kindustry.market.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.kindustry.market.ui.component.EquityIndicator
import com.kindustry.market.ui.component.EquityBasicInfo
import com.kindustry.market.ui.component.EquityList
import com.kindustry.market.ui.component.ScrollableTable
import com.kindustry.market.ui.component.SearchDialog
import com.kindustry.market.ui.component.SideDrawer
import com.kindustry.market.ui.component.WebViewChart
import com.kindustry.market.viewmodel.MainViewModel
import kotlin.math.abs

// 创建一个提供 PaddingValues 的父组件，然后所有子组件都可以使用 LocalPaddingValues.current 访问这些内边距
val LocalPaddingValues = staticCompositionLocalOf<PaddingValues> { error("No PaddingValues provided") }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    onSearchClick: (String) -> Unit,
    onListClick: (List<Any>) -> Unit,
    onPreviewClick: (String) -> Unit,
//    onChartClick: (String, String) -> Unit,
    onInfoClick: (String) -> Unit,
    onFavoriteClick: () -> Unit
){
    // 使用 remember 保存状态
    val screenState = remember { mutableStateOf(ScreenState.A) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var queryCondition:List<Any> by remember { mutableStateOf(listOf()) }

    // 获取一次  市场区分
    val exchangeList = mainViewModel.allExchangeFlow.collectAsState(initial = emptyList()).value
    val sectorList = mainViewModel.allSectorFlow.collectAsState(initial = emptyList()).value

    // 订阅 equitysFlow 并更新 equitys 列表
    val equityInfo by mainViewModel.equityInfoFlow.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                  Text(text = "${equityInfo?.symbol?: ""}  ${equityInfo?.name?: ""}" )
                },
                navigationIcon = {
                    IconButton(onClick = { /* 处理菜单点击事件 */
                        showFilterDialog = true
                    }) {
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = "FilterList")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showSearchDialog = true
                    }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )

            // 銘柄検索
            if (showSearchDialog) {
                SearchDialog {
                    if (it.trim() != "") {
                        onSearchClick(it.trim())
                        screenState.value = ScreenState.A
                    }
                    showSearchDialog = false
                }
            }

            // 条件検索
            if (showFilterDialog) {
                Dialog(
                    onDismissRequest = { showFilterDialog = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false), // 禁用平台默认宽度
                    content = {
                        SideDrawer(
                            exchangeList,
                            sectorList,
                            queryCondition
                        ) { condition: List<Any> ->
                            showFilterDialog = false
                            // Handle the case where the list is empty
                            if (condition.isNotEmpty()) {
                                queryCondition = condition
                                onListClick(queryCondition)
                                screenState.value = ScreenState.A
//                                symbol = ""
//                                name = ""
                            }
                        }
                    }
                )
            }

        },
        bottomBar = {  // 底部菜单项
            BottomNavigation {
                BottomNavigationItem(
                    selected = true,
                    onClick = {
//                        onListClick(emptyList<Any>())
                        screenState.value = ScreenState.A
                    } ,
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = "ListAlt") },
                    label = { Text(text = "List") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = {
                        equityInfo?.symbol?.let { onPreviewClick(it) }
                        screenState.value = ScreenState.B
                    } ,
                    icon = { Icon(Icons.Default.Preview, contentDescription = "Preview") },
                    label = { Text(text = "Preview") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick =  {
                        //  TODO  onChartClick("","")
                        screenState.value = ScreenState.C
                     } ,
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "BarChart") },
                    label = { Text(text = "Chart") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = {
                        equityInfo?.symbol?.let { onInfoClick(it) }
                        screenState.value = ScreenState.D
                    },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Info") },
                    label = { Text(text = "Info") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = {
                        onFavoriteClick()
                        screenState.value = ScreenState.E
//                        navController.navigate("login")
                    } ,
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite") },
                    label = { Text(text = "Favorite") }
                )
            }
        }
    ){
        paddingValues ->
        CompositionLocalProvider(LocalPaddingValues provides paddingValues) {
            // Pass the data and function as props to SimpleColumn
            when (screenState.value) {
                ScreenState.A -> EquityList(
//                    equitys = equitys ,
                    viewModel = mainViewModel
                ) /*{ firstParam: String, secondParam: String ->
                    symbol = firstParam
                    name = secondParam
                }*/

                ScreenState.B -> EquityIndicator(mainViewModel)
                ScreenState.C -> WebViewChart(mainViewModel) // equityInfo?.symbol?.let { WebViewChart(it) }
                ScreenState.D -> EquityBasicInfo(mainViewModel)
                ScreenState.E -> ScrollableTable(mainViewModel  // MyFavorite(equities)
                ) { firstParam: String, secondParam: String ->
//                    symbol = firstParam
//                    name = secondParam
                }  //  MyFavorite(equitys)
            }
        }

    }
}

enum class ScreenState {
    A, B, C, D, E
}


// 水平方向滑动
fun Modifier.onHorizontalSwipe(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    threshold: Float = 20f  // 获取第一个触摸点的位移量
): Modifier = composed {
    pointerInput(Unit) {
        awaitPointerEventScope {
            var totalHorizontalDrag = 0f
            var totalVerticalDrag = 0f
            val pass = PointerEventPass.Final  // 在事件被传递给所有主要消费者和任何其他监听器之后等待
            while (true) {
                val event = awaitPointerEvent(pass = pass)
                // 获取第一个触摸点的位移量
                val dragChange = event.changes.firstOrNull()?.positionChange() ?: continue
                // 累积拖动的距离
                totalHorizontalDrag += dragChange.x
                totalVerticalDrag += dragChange.y

                // 判断触摸是否已经抬起（拖动结束）
                // event.changes.size == 1 单指滑动检测，单指正在移动时才执行后续的滑动距离累积和事件消费操作。
                if (event.changes.size == 1  &&  event.changes.first().pressed.not()) {
                    // abs(totalHorizontalDrag) > threshold 阈值，避免误触
                    // abs(totalHorizontalDrag) > abs(totalVerticalDrag) * 2 只在水平滑动大于垂直滑动一定程度时才触发
                    if ((abs(totalHorizontalDrag) > threshold) && (abs(totalHorizontalDrag) > abs(totalVerticalDrag) * 2)) {
//                    if (abs(totalHorizontalDrag) > threshold.dp.toPx()) {
                        if (totalHorizontalDrag > 0) {
                            // 滑动到右边
                            onSwipeRight()
                        } else if (totalHorizontalDrag < 0){
                            // 滑动到左边
                            onSwipeLeft()
                        }
                    }
                    totalHorizontalDrag = 0f
                    totalVerticalDrag = 0f

                    // 事件消费
                    event.changes.forEach { it.consume() }
                }
            }
        }
    }
}







