package com.kindustry.market.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kindustry.market.db.entity.Equity
import com.kindustry.market.ui.screen.LocalPaddingValues

@Composable
fun EquityIndicator(equity: Equity?){

    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier.padding(LocalPaddingValues.current)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "株価指標", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//        Text(text = "もっと見る", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    IndicatorItem("市場区分", equity?.exchange)
                    IndicatorItem("上場日", equity?.listingDate)
                    IndicatorItem("日経業種分類", equity?.industry)
                    IndicatorItem("東証業種名", equity?.sector)
                    IndicatorItem("株価収益率", equity?.per, "倍")
                    IndicatorItem("株価純資産倍率", equity?.pbr, "倍")
                    IndicatorItem("総資産利益率", equity?.roa, "%")
                    IndicatorItem("配当利回り", equity?.dividendYield, "%")
                    IndicatorItem("負債比率", equity?.debtAssetRatio, "%")
                    IndicatorItem("自己資本比率", equity?.ownCapitalRatio, "%")
                }
                Column(modifier = Modifier.weight(1f)) {
                    IndicatorItem("年初来株価上昇率", equity?.yearChangeRatio, "%")
                    IndicatorItem("移動平均乖離率", equity?.movingAverageRatio, "%")
                    IndicatorItem("現在株価", equity?.presentPrice)
                    IndicatorItem("年初来安値", equity?.yearLow)
                    IndicatorItem("年初来高値", equity?.yearHigh)
                    IndicatorItem("移動平均線", equity?.movingAverage)
                    IndicatorItem("1株純資産", equity?.bookValuePerShare)
                    IndicatorItem("出来高", equity?.volume)
                    IndicatorItem("売買回転率", equity?.turnoverRate, "‰")
                    IndicatorItem("時価総額", equity?.marketCap, "億円")
                }
            }
        }

    }
}


@Composable
fun IndicatorItem(label: String, value: Any?, unit: String? = "") {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$label：",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colors.secondaryVariant
        )
        Text(
            textAlign = TextAlign.End,   // 设置文本右对齐
//            text = value?.toString() ?: "",
            text = value?.let { "${it.toString()}$unit" } ?: "",
            fontSize = 14.sp,
            style = MaterialTheme.typography.body2,
//            maxLines = if(isExpanded) Int.MAX_VALUE else 1
       )
    }
}
