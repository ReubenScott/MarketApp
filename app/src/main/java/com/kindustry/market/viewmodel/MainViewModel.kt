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
    val symbol: String,
    val name: String
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {
    val readAll = companyRepository.readAll

    // 直接转换 readAll Flow
    val stockInfoList: StateFlow<List<StockInfo>> = companyRepository.readAll.map { stockList ->
        stockList.map { stock ->
            stock.toStockInfo() // 使用扩展函数进行转换
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // Flow<List<Stock>>
    private val _stocks = MutableStateFlow<List<Stock>>(emptyList())
    val stocks: StateFlow<List<Stock>> = _stocks.asStateFlow()

    fun randomGet() {
        viewModelScope.launch {
            _stocks.value = companyRepository.randomStocks.first()
        }
    }


    private val _uiState = MutableStateFlow<List<StockInfo>>(emptyList())
    val stockListState: StateFlow<List<StockInfo>> = _uiState.asStateFlow()

    fun getRandomStock() {
        viewModelScope.launch {
            companyRepository.randomStocks
                .map { stockList ->
                    val stockInfo = stockList.firstOrNull()?.toStockInfo()
                    if (stockInfo != null) listOf(stockInfo) else emptyList() // Create a list
                }
                .collect { stockInfoList  ->
                    _uiState.value = stockInfoList
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


    // 扩展函数，方便转换
    fun Stock.toStockInfo(): StockInfo {
        return StockInfo(symbol, name ?: "") // 处理 name 为 null 的情况
    }

}