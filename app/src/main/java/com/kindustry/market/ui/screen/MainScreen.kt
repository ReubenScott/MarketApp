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
import com.kindustry.market.ui.component.CompanyInfo
import com.kindustry.market.ui.component.MyFavorite
import com.kindustry.market.ui.component.StockList
import com.kindustry.market.ui.component.SideDrawer
import com.kindustry.market.viewmodel.MainViewModel

val LocalPaddingValues = staticCompositionLocalOf<PaddingValues> { error("No PaddingValues provided") }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    reopened: String = "false",
    mainViewModel: MainViewModel,
    onListClick: (List<Any>) -> Unit,
    onPreviewClick: (String) -> Unit,
    onChartClick: (String, String) -> Unit,
    onInfoClick: (String) -> Unit,
    onFavoriteClick: () -> Unit
){
    // 使用 remember 保存状态
    var showDialog by remember { mutableStateOf(false) }
    val screenState = remember { mutableStateOf(ScreenState.A) }

    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    // 获取一次  市场区分
    val exchangeList = mainViewModel.allExchangeFlow.collectAsState(initial = emptyList()).value
    val sectorList = mainViewModel.allSectorFlow.collectAsState(initial = emptyList()).value

    // 订阅 stocksFlow 并更新 stocks 列表
    val stocks by mainViewModel.stockListFlow.collectAsState()
    val stock by mainViewModel.stockFlow.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                  Text(text = "${symbol}  ${name}" )
                },
                navigationIcon = {
                    IconButton(onClick = { /* 处理菜单点击事件 */
                        showDialog = true
                    }) {
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = "FilterList")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )

            // TODO
            if (showDialog) {
//                AlertDialog(
//                    onDismissRequest = { showDialog = false },
//                    title = { Text(text = "这是一个浮动窗口") },
//                    text = { Text(text = "你可以在这里显示一些内容") },
//                    confirmButton = {
//                        Button(onClick = { showDialog = false }) {
//                            Text(text = "确定")
//                        }
//                    }
//                )
                Dialog(
                    onDismissRequest = { showDialog = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false), // 禁用平台默认宽度
                    content = {
                        SideDrawer(
                            exchangeList,
                            sectorList,
                            { firstParam:String, secondParam:String ->
                                showDialog = false
                                onListClick( listOf(firstParam, secondParam) )
                                screenState.value = ScreenState.A
                                symbol = ""
                                name = ""
                            }
                        )
//                        DropdownMenuExample()
//                        DropdownMenuWithDescription()
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
                    onClick =  { onChartClick("","") }  , //  TODO
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
                ScreenState.A -> StockList(
                    stocks = stocks ,
                    { firstParam:String, secondParam:String ->
                        symbol = firstParam
                        name = secondParam
                    }
                )
                ScreenState.B -> MyFavorite(stocks)
                ScreenState.C -> MyFavorite(stocks)
                ScreenState.D -> CompanyInfo(stock)
                ScreenState.E -> MyFavorite(stocks)
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






