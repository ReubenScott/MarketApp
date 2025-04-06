package com.kindustry.market.ui.component

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.kindustry.market.ui.screen.onHorizontalSwipe
import com.kindustry.market.viewmodel.MainViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewChart(
  mainViewModel: MainViewModel
) {
    // 订阅 equitysFlow 并更新 equitys 列表
    val equityInfo by mainViewModel.equityInfoFlow.collectAsState()
    val scrollState = rememberScrollState()

    // https://www.nikkei.com/nkd/company/chart/?type=10year&scode=6619&ba=1
    // https://minkabu.jp/stock/6619/chart
    // https://kabutan.jp/stock/chart?code=6619
    // https://kabuyoho.jp/reportChart?bcode=6619
    // https://finance.yahoo.co.jp/quote/6619.T/chart
    // https://jp.tradingview.com/chart/?symbol=TSE:6619

    val url = "https://www.nikkei.com/nkd/company/chart/?type=10year&scode=${equityInfo?.symbol}&ba=1"

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true // 启用 JavaScript (如果需要)
                webViewClient = WebViewClient() // 处理页面导航
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url) // 如果 URL 发生变化，重新加载
        },
        modifier = Modifier.fillMaxWidth()
            .verticalScroll(scrollState) // 添加垂直滚动
            .onHorizontalSwipe(
                onSwipeLeft = { mainViewModel.swipeScreenEquity(1) },
                onSwipeRight = { mainViewModel.swipeScreenEquity(-1) }
            )
    )
}

