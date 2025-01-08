package com.kindustry.market.screen


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.kindustry.market.R
import com.kindustry.market.db.entity.Stock
import com.kindustry.market.ui.component.SideDrawer
import com.kindustry.market.viewmodel.StockInfo


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPage(navController: NavController, reopened: String = "false", stocks: List<Stock>, onButtonClick: () -> Unit){

    var showDialog by remember { mutableStateOf(false) }

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
                }
                , actions = {
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
                    }
                )
            }
        },
        bottomBar = {  // 底部菜单项
            BottomNavigation {
                BottomNavigationItem(
                    selected = true,
                    onClick = onButtonClick,
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = "ListAlt") },
                    label = { Text(text = "List") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = { /* Handle click */ },
                    icon = { Icon(Icons.Default.Preview, contentDescription = "Preview") },
                    label = { Text(text = "Preview") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = { /* Handle click */ }, // Trigger refresh on home button click
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "BarChart") },
                    label = { Text(text = "Chart") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = { /* Handle click */ },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Info") },
                    label = { Text(text = "Info") }
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = { /* Handle click */ },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite") },
                    label = { Text(text = "Favorite") }
                )
            }
        }
    ){
        // Pass the data and function as props to SimpleColumn
        BodyContent(stocks = stocks)
    }
}

@Composable
fun BodyContent(stocks: List<Stock>){
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState) ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (stocks.isNotEmpty()){
            for (company in stocks){
                Row() {
                    Text(
                        text = company.symbol,
                    )
                    Text(
                        text = company.name ?: "", // 当 name 为 null 时，显示默认值
                    )
                }

            }
        }
    }
}


@Composable
fun Conversation(messages : List<StockInfo>){
    LazyColumn(){
        items(messages){
                message -> MessageCard(message)
        }
    }
}




@Composable
fun MessageCard(msg: StockInfo) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.snap1),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember {
            mutableStateOf(false )
        }
        val surfaceColor: Color by animateColorAsState(
            if(isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        )
        Column (
            modifier = Modifier.clickable {isExpanded = !isExpanded}
        ){
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.symbol, color = MaterialTheme.colors.secondaryVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = surfaceColor,
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.name,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2,
                    maxLines = if(isExpanded) Int.MAX_VALUE else 1
                )
            }
        }
    }
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






