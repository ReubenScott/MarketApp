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
    val pbr: Float?  // 株価純資産倍率
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
            debtEquityRatio?.let{
                it / (it + 100f) * 100f  // 债务权益比率　から計算
            } ,
            per ,
            pbr
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
            var equitys =  _equityListState.value
            equitys = when (sortColumn) {
                "name" -> if (isAscending) equitys.sortedBy { it.name } else equitys.sortedByDescending { it.name }
                "sector" -> if (isAscending) equitys.sortedBy { it.sector } else equitys.sortedByDescending { it.sector }
                "dividendYield" -> if (isAscending) equitys.sortedBy { it.dividendYield } else equitys.sortedByDescending { it.dividendYield }
                "debtAssetRatio" -> if (isAscending) equitys.sortedBy { it.debtAssetRatio } else equitys.sortedByDescending { it.debtAssetRatio }
                "per" -> if (isAscending) equitys.sortedBy { it.per } else equitys.sortedByDescending { it.per }
                "pbr" -> if (isAscending) equitys.sortedBy { it.pbr } else equitys.sortedByDescending { it.pbr }
                else -> if (isAscending) equitys.sortedBy { it.symbol } else equitys.sortedByDescending { it.symbol } // 默认按 symbol 排序
            }
            _equityListState.value = equitys
        }
    }

//    fun searchEquitys(exchange: String, sector: String): StateFlow<List<EquityInfo>> {
//        viewModelScope.launch {
//            _equityState.value = companyRepository.getQueryEquitys(exchange, sector).first().map{ it.toEquityInfo() }  // 使用扩展函数进行转换
//        }
//        return equitysFlow
//    }


    fun filterEquitys(any: List<Any>) {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityListState.value = companyRepository.getQueryEquitys(any).first().map{ it.toEquityInfo() }  // 使用扩展函数进行转换
        }
    }


    //  Equity Flow
    private val _equityState = MutableStateFlow<Equity?>(null)
    val equityFlow: StateFlow<Equity?> = _equityState.asStateFlow()

    fun findEquitys(symbol: String) {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityState.value = companyRepository.getQueryEquitys(symbol).first()  // 使用扩展函数进行转换
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