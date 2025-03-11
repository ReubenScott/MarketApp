package com.kindustry.market.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.kindustry.market.R
import com.kindustry.market.ui.component.MyFavorite
import com.kindustry.market.ui.component.StockList
import com.kindustry.market.ui.component.SideDrawer
import com.kindustry.market.viewmodel.MainViewModel
import com.kindustry.market.viewmodel.StockInfo


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    reopened: String = "false",
    mainViewModel: MainViewModel,
//    stocks: List<StockInfo>,
    onListClick: () -> Unit,
    onPreviewClick: () -> Unit,
    onChartClick: () -> Unit,
    onInfoClick: () -> Unit,
    onFavoriteClick: () -> Unit
){
    // 使用 remember 保存状态
    var showDialog by remember { mutableStateOf(false) }
    val screenState = remember { mutableStateOf(ScreenState.A) }

    // 订阅 stocksFlow 并更新 stocks 列表
    val stocks by mainViewModel.stocksFlow.collectAsState()

//    LaunchedEffect(Unit) {
//        mainViewModel.stocksFlow.collect { newStocks ->
//            stocks.clear()
//            stocks.addAll(newStocks)
//        }
//    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                  Text(text = "LayoutStudy")
                },
                navigationIcon = {
                    IconButton(onClick = { /* 处理菜单点击事件 */
                        showDialog = true
                    }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "FilterList")
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
                        SideDrawer()
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
                        onListClick()
                        screenState.value = ScreenState.A
                    } ,
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = "ListAlt") },
                    label = { Text(text = "List") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = onPreviewClick ,
                    icon = { Icon(Icons.Default.Preview, contentDescription = "Preview") },
                    label = { Text(text = "Preview") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = onChartClick , // Trigger refresh on home button click
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "BarChart") },
                    label = { Text(text = "Chart") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = onInfoClick ,
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
        // Pass the data and function as props to SimpleColumn
        when (screenState.value) {
            ScreenState.A -> StockList(stocks = stocks)
            ScreenState.B -> MyFavorite(stocks)
            ScreenState.C -> MyFavorite(stocks)
            ScreenState.D -> MyFavorite(stocks)
            ScreenState.E -> MyFavorite(stocks)
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






