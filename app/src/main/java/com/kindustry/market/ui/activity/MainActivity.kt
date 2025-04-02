package com.kindustry.market.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kindustry.market.ui.screen.LoginScreen
import com.kindustry.market.ui.screen.MainScreen
import com.kindustry.market.ui.theme.MarketTheme
import com.kindustry.market.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission(this, this) // 使用 Activity 的 Context

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
                                onSearchClick = { codeOrName:String -> mainViewModel.findEquityBySymbolOrName(codeOrName) },
                                onListClick = { data: List<Any> -> mainViewModel.filterEquities(data) },
                                onPreviewClick = { symbol:String -> mainViewModel.findEquityBySymbol(symbol) },
//                                onChartClick = { /* firstParam:String, secondParam:String -> mainViewModel.filterEquities(listOf(firstParam, secondParam)) */ },
                                onInfoClick = { symbol:String -> mainViewModel.findEquityBySymbol(symbol)},
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


    // 在 Activity 中处理权限请求结果
/*    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予
                // 执行需要存储权限的操作
            } else {
                // 权限被拒绝
                // 处理权限被拒绝的情况
            }
        }
    }*/

}

//获取外部存储管理权限
private const val REQUEST_STORAGE_PERMISSION = 123 // 请求码
fun requestPermission(context: Context, activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // Android 11 及更高版本
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    } else {
        // Android 10 及更低版本
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest, REQUEST_STORAGE_PERMISSION)
        }
    }
}
