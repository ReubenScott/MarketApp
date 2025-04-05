package com.kindustry.market.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.kindustry.market.component.EquityIndicator
import com.kindustry.market.ui.component.EquityBasicInfo
import com.kindustry.market.ui.component.EquityList
import com.kindustry.market.ui.component.ScrollableTable
import com.kindustry.market.ui.component.SearchDialog
import com.kindustry.market.ui.component.SideDrawer
import com.kindustry.market.ui.component.WebViewChart
import com.kindustry.market.viewmodel.MainViewModel

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
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    val screenState = remember { mutableStateOf(ScreenState.A) }

    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var queryCondition:List<Any> by remember { mutableStateOf(listOf()) }

    // 获取一次  市场区分
    val exchangeList = mainViewModel.allExchangeFlow.collectAsState(initial = emptyList()).value
    val sectorList = mainViewModel.allSectorFlow.collectAsState(initial = emptyList()).value

    // 订阅 equitysFlow 并更新 equitys 列表
    val equity by mainViewModel.equityFlow.collectAsState()



    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                  Text(text = "${symbol}  ${name}" )
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
                                symbol = ""
                                name = ""
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
                        onPreviewClick(symbol)
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
                        onInfoClick(symbol)
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
                ) { firstParam: String, secondParam: String ->
                    symbol = firstParam
                    name = secondParam
                }

                ScreenState.B -> EquityIndicator(equity)
                ScreenState.C ->  WebViewChart(symbol)
                ScreenState.D -> EquityBasicInfo(equity)
                ScreenState.E -> ScrollableTable(mainViewModel  // MyFavorite(equities)
                ) { firstParam: String, secondParam: String ->
                    symbol = firstParam
                    name = secondParam
                }  //  MyFavorite(equitys)
            }
        }

    }
}

enum class ScreenState {
    A, B, C, D, E
}





//@Composable
//fun Conversation2(messages : LiveData<Message>){
//    var msgs by messages.observeAsState()
//    LazyColumn(){
//        items(messages){
//                message -> MessageCard(message)
//        }
//    }
//}






