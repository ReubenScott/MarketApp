package com.kindustry.market.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindustry.market.db.entity.Equity
import com.kindustry.market.db.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


// 定义 UI 状态数据类
data class EquityInfo(
    val symbol: String,  // コード
    val name: String,    // 銘柄名
    val sector: String,  // 東証業種名
    val dividendYield: Float?,  // 配当利回り
    val debtAssetRatio: Float?,  // 負債比率  (债务权益比率debtEquityRatioから計算)
    val per: Float?,  // 株価収益率
    val pbr: Float?,  // 株価純資産倍率
    val yearChangeRatio: Float?,  // 年初来株価上昇率
    val movingAverageRatio: Float?,  // 200日移動平均乖離率
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    // 扩展函数，方便转换
    fun Equity.toEquityInfo(): EquityInfo {
        return EquityInfo(
            symbol ,
            name ?: "" ,  //  ?: ""处理 name 为 null 的情况
            sector ?: "" ,
            dividendYield ,
            debtAssetRatio ,
            per ,
            pbr ,
            yearChangeRatio ,
            movingAverageRatio ,
        )
    }

    //  Equity Flow
    private val _equityListState = MutableStateFlow<List<EquityInfo>>(emptyList())
    val equityListFlow: StateFlow<List<EquityInfo>> = _equityListState.asStateFlow()

    fun randomGet() {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityListState.value = companyRepository.randomEquitys.first().map{ it.toEquityInfo() }  // 使用扩展函数进行转换
        }
    }

    fun getRandomEquity() {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            companyRepository.randomEquitys
                .map { equityList ->
                    val EquityInfo = equityList.firstOrNull()?.toEquityInfo()
                    if (EquityInfo != null) listOf(EquityInfo) else emptyList() // Create a list
                }
                .collect { EquityInfoList  ->
                    _equityListState.value = EquityInfoList
                }
        }
    }

    private val _scrollPosition = MutableStateFlow(0)
    val scrollPosition: StateFlow<Int> = _scrollPosition

    fun setScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    //  Sort Flow
    private val _isAscendingState = MutableStateFlow(true)
    val isAscendingFlow: StateFlow<Boolean> = _isAscendingState.asStateFlow()

    fun updateIsAscending(value: Boolean) {
        viewModelScope.launch {
            _isAscendingState.value = value
        }
    }

    private val _sortColumnState = MutableStateFlow("")
    val sortColumnFlow: StateFlow<String> = _sortColumnState.asStateFlow()

    fun updateSortColumn(value: String) {
        viewModelScope.launch {
            _sortColumnState.value = value
        }
    }

    fun sortEquityInfo(sortColumn: String, isAscending: Boolean) {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            var equities =  _equityListState.value
            equities = when (sortColumn) {
                "name" -> if (isAscending) equities.sortedBy { it.name } else equities.sortedByDescending { it.name }
                "sector" -> if (isAscending) equities.sortedBy { it.sector } else equities.sortedByDescending { it.sector }
                "dividendYield" -> if (isAscending) equities.sortedBy { it.dividendYield } else equities.sortedByDescending { it.dividendYield }
                "debtAssetRatio" -> if (isAscending) equities.sortedBy { it.debtAssetRatio } else equities.sortedByDescending { it.debtAssetRatio }
                "per" -> if (isAscending) equities.sortedBy { it.per } else equities.sortedByDescending { it.per }
                "pbr" -> if (isAscending) equities.sortedBy { it.pbr } else equities.sortedByDescending { it.pbr }
                "yearChangeRatio" -> if (isAscending) equities.sortedBy { it.yearChangeRatio } else equities.sortedByDescending { it.yearChangeRatio }
                "movingAverageRatio" -> if (isAscending) equities.sortedBy { it.movingAverageRatio } else equities.sortedByDescending { it.movingAverageRatio }
                else -> if (isAscending) equities.sortedBy { it.symbol } else equities.sortedByDescending { it.symbol } // 默认按 symbol 排序
            }
            _equityListState.value = equities
        }
    }

//    fun searchEquitys(exchange: String, sector: String): StateFlow<List<EquityInfo>> {
//        viewModelScope.launch {
//            _equityState.value = companyRepository.getQueryEquitys(exchange, sector).first().map{ it.toEquityInfo() }  // 使用扩展函数进行转换
//        }
//        return equitysFlow
//    }


    fun filterEquities(any: List<Any>) {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityListState.value = companyRepository.getQueryEquities(any).first().map{ it.toEquityInfo() }  // 使用扩展函数进行转换
        }
    }

    fun findEquityBySymbolOrName(codeOrName: String) {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityListState.value = companyRepository.findEquityBySymbolOrName(codeOrName).first().map{ it.toEquityInfo() } // 使用扩展函数进行转换
        }
    }

    //  Equity Flow
    private val _equityState = MutableStateFlow<Equity?>(null)
    val equityFlow: StateFlow<Equity?> = _equityState.asStateFlow()

    fun findEquityBySymbol(symbol: String) {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityState.value = companyRepository.findEquityBySymbol(symbol).first()  // 使用扩展函数进行转换
        }
    }


    // 全部 市场区分
    val allExchangeFlow: Flow<List<String>> = companyRepository.allExchange

    // 全部 業種
    val allSectorFlow: Flow<List<String>> = companyRepository.allSector

    // 直接转换 readAll Flow
   /*
   val EquityInfoList: StateFlow<List<EquityInfo>> = companyRepository.readAll.map { equityList ->
        equityList.map { equity ->
            equity.toEquityInfo() // 使用扩展函数进行转换
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    */



}