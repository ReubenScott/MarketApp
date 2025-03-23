package com.kindustry.market.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.kindustry.market.ui.screen.MainScreen
import com.kindustry.market.ui.screen.LoginScreen
import com.kindustry.market.ui.theme.MarketTheme
import com.kindustry.market.viewmodel.MainViewModel
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
//                val equitys = mainViewModel.equitys.collectAsState(initial = emptyList()).value

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    MainPage(
//                        equitys = mainViewModel.equitys.collectAsState(initial = emptyList()).value,
//                        onButtonClick = { mainViewModel.randomGet() }
//                    )

                    // 设置 Navigation Host
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") { // 定义 "main" 路由
                            MainScreen(
                                navController ,// 将 NavController 传递给 MainPage
//                                equitys = mainViewModel.equitysFlow.collectAsState(initial = emptyList()).value,
                                mainViewModel = mainViewModel,
                                onListClick = { any: List<Any> -> mainViewModel.filterEquitys(any) },
                                onPreviewClick = { firstParam:String -> mainViewModel.findEquitys(firstParam) },
                                onChartClick = { firstParam:String, secondParam:String -> mainViewModel.filterEquitys(listOf(firstParam, secondParam)) },
                                onInfoClick = { firstParam:String -> mainViewModel.findEquitys(firstParam)},
                                onFavoriteClick = { mainViewModel.randomGet()}
                            )
                        }
                        composable("login") { backStackEntry -> // 定义带有参数的 "main" 路由
                            val reopened = backStackEntry.arguments?.getString("reopened") ?: "false"
                            LoginScreen(
                                navController //, reopened = reopened
                            )
                        }
                    }
                }


            }

        }
    }

}



//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MarketTheme {
//        MessageCard(EquityInfo(symbol = "Android", name = "Jectpack"))
//    }
//}

