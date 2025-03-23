package com.kindustry.market.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kindustry.market.viewmodel.EquityInfo

// 使用示例
@Composable
fun MyFavorite(equitys: List<EquityInfo>) {
    val outerList = listOf(
        listOf("Item 1", "Item 2", "Item 3"),
        listOf("Item A", "Item B", "Item C", "Item D"),
        listOf("One", "Two", "Three", "Four", "Five")
    )
    val stringList = equitys.map {
        listOf(it.symbol, it.name, it.sector, it.dividendYield?.toString(), it.debtAssetRatio?.toString(), it.per?.toString(), it.pbr?.toString())
    }
    CombinedList(outerItems = stringList)
}

@Composable
fun CombinedList(outerItems: List<List<String?>>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(outerItems) { innerItems ->
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(innerItems) { item ->
                    Spacer(modifier = Modifier.wrapContentWidth()) // 添加一个 Spacer 来分隔字段名和内容
                    Text(
                        text = item?:"",
                        modifier = Modifier.wrapContentWidth()
                    )

                }
            }
        }
    }
}
