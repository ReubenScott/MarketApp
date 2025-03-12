package com.kindustry.market.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindustry.market.db.entity.Stock
import com.kindustry.market.db.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


// 定义 UI 状态数据类
data class StockInfo(
    val symbol: String,  // コード
    val name: String,    // 銘柄名
    val sector: String,  // 東証業種名
    val dividendYield: Float?,  // 配当利回り
    val debtAssetRatio: Float?,  // 負債比率  (债务权益比率debtEquityRatioから計算)
    val per: Float?,  // 株価収益率
    val pbr: Float?  // 株価純資産倍率
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    // 扩展函数，方便转换
    fun Stock.toStockInfo(): StockInfo {
        return StockInfo(
            symbol ,
            name ?: "" ,  //  ?: ""处理 name 为 null 的情况
            sector ?: "" ,
            dividendYield ,
            debtEquityRatio?.let{
                it / (it + 100f) * 100f  // 债务权益比率　から計算
            } ,
            per ,
            pbr
        )
    }

    //  Stock Flow
    private val _stockState = MutableStateFlow<List<StockInfo>>(emptyList())
    val stocksFlow: StateFlow<List<StockInfo>> = _stockState.asStateFlow()

    fun randomGet() {
        viewModelScope.launch {
            _stockState.value = companyRepository.randomStocks.first().map{ it.toStockInfo() }  // 使用扩展函数进行转换
        }
    }

    // 全部 市场区分
    val allExchangeFlow: Flow<List<String>> = companyRepository.allExchange

    // 全部 業種
    val allSectorFlow: Flow<List<String>> = companyRepository.allSector

    // 直接转换 readAll Flow
   /*
   val stockInfoList: StateFlow<List<StockInfo>> = companyRepository.readAll.map { stockList ->
        stockList.map { stock ->
            stock.toStockInfo() // 使用扩展函数进行转换
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    */

    fun searchStocks(exchange: String, sector: String): StateFlow<List<StockInfo>> {
        viewModelScope.launch {
            _stockState.value = companyRepository.getQueryStocks(exchange, sector).first().map{ it.toStockInfo() }  // 使用扩展函数进行转换
        }
        return stocksFlow
    }

    fun getRandomStock() {
        viewModelScope.launch {
            companyRepository.randomStocks
                .map { stockList ->
                    val stockInfo = stockList.firstOrNull()?.toStockInfo()
                    if (stockInfo != null) listOf(stockInfo) else emptyList() // Create a list
                }
                .collect { stockInfoList  ->
                    _stockState.value = stockInfoList
                }
        }
    }



    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun incrementCount() {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _count.value++
        }
    }


}