package com.kindustry.market.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.kindustry.market.screen.MainPage
import com.kindustry.market.screen.MessageCard
import com.kindustry.market.ui.page.LoginPage
import com.kindustry.market.ui.theme.MarketTheme
import com.kindustry.market.viewmodel.MainViewModel
import com.kindustry.market.viewmodel.StockInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //让内容，显示在状态栏和系统导航栏后面：状态栏和导航栏会遮盖部分内容
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MarketTheme {
                val navController = rememberNavController() // 创建 NavController

//                val result by mainViewModel.readAll.collectAsState(initial = emptyList())
//                val stocks = mainViewModel.stocks.collectAsState(initial = emptyList()).value

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    MainPage(
//                        stocks = mainViewModel.stocks.collectAsState(initial = emptyList()).value,
//                        onButtonClick = { mainViewModel.randomGet() }
//                    )

                    // 设置 Navigation Host
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") { // 定义 "main" 路由
                            MainPage(
                                navController ,// 将 NavController 传递给 MainPage
                                stocks = mainViewModel.stocks.collectAsState(initial = emptyList()).value,
                                onButtonClick = { mainViewModel.randomGet() }
                            )
                        }
                        composable("login") { backStackEntry -> // 定义带有参数的 "main" 路由
                            val reopened = backStackEntry.arguments?.getString("reopened") ?: "false"
                            LoginPage(
                                navController //, reopened = reopened
                            )
                        }
                    }
                }


            }

        }
    }

}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MarketTheme {
        MessageCard(StockInfo(symbol = "Android", name = "Jectpack"))
    }
}

