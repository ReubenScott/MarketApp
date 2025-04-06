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
    val exchange: String,  // 市場区分
    val listingDate: String,  // 上場日
    val sector: String,  // 東証業種名
    val presentPrice: Float?,  // 現在株価
    val dividendYield: Float?,  // 配当利回り
    val debtAssetRatio: Float?,  // 負債比率  (债务权益比率debtEquityRatioから計算)
    val per: Float?,  // 株価収益率
    val pbr: Float?,  // 株価純資産倍率
    val yearChangeRatio: Float?,  // 年初来株価上昇率
    val movingAverageRatio: Float?,  // 200日移動平均乖離率
    val marketCap: Float?,  // 時価総額 億円
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    // 扩展函数，方便转换
    private fun Equity.toEquityInfo(): EquityInfo {
        return EquityInfo(
            symbol ,
            name ?: "" ,  //  ?: ""处理 name 为 null 的情况
            exchange ?: "" ,
            listingDate ?: "" ,
            sector ?: "" ,
            presentPrice ,
            dividendYield ,
            debtAssetRatio ,
            per ,
            pbr ,
            yearChangeRatio ,
            movingAverageRatio ,
            marketCap ,
        )
    }

    //  EquityInfo Flow
    private val _equityInfoListState = MutableStateFlow<List<EquityInfo>>(emptyList())
    val equityInfoListFlow: StateFlow<List<EquityInfo>> = _equityInfoListState.asStateFlow()

    fun randomGet() {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityInfoListState.value = companyRepository.randomEquitys.first().map{ it.toEquityInfo() }  // 使用扩展函数进行转换
        }
    }

    fun getRandomEquity() {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            companyRepository.randomEquitys
                .map { equityList ->
                    val equityItem = equityList.firstOrNull()?.toEquityInfo()
                    if (equityItem != null) listOf(equityItem) else emptyList() // Create a list
                }
                .collect { EquityInfoList  ->
                    _equityInfoListState.value = EquityInfoList
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
            var equities =  _equityInfoListState.value
            equities = when (sortColumn) {
                "name" -> if (isAscending) equities.sortedBy { it.name } else equities.sortedByDescending { it.name }
                "exchange" -> if (isAscending) equities.sortedBy { it.exchange } else equities.sortedByDescending { it.exchange }
                "listingDate" -> if (isAscending) equities.sortedBy { it.listingDate } else equities.sortedByDescending { it.listingDate }
                "sector" -> if (isAscending) equities.sortedBy { it.sector } else equities.sortedByDescending { it.sector }
                "yearChangeRatio" -> if (isAscending) equities.sortedBy { it.yearChangeRatio } else equities.sortedByDescending { it.yearChangeRatio }
                "movingAverageRatio" -> if (isAscending) equities.sortedBy { it.movingAverageRatio } else equities.sortedByDescending { it.movingAverageRatio }
                "debtAssetRatio" -> if (isAscending) equities.sortedBy { it.debtAssetRatio } else equities.sortedByDescending { it.debtAssetRatio }
                "per" -> if (isAscending) equities.sortedBy { it.per } else equities.sortedByDescending { it.per }
                "pbr" -> if (isAscending) equities.sortedBy { it.pbr } else equities.sortedByDescending { it.pbr }
                "dividendYield" -> if (isAscending) equities.sortedBy { it.dividendYield } else equities.sortedByDescending { it.dividendYield }
                "marketCap" -> if (isAscending) equities.sortedBy { it.marketCap } else equities.sortedByDescending { it.marketCap }
                else -> if (isAscending) equities.sortedBy { it.symbol } else equities.sortedByDescending { it.symbol } // 默认按 symbol 排序
            }
            _equityInfoListState.value = equities
        }
    }

    //  EquityInfo Flow
    private val _equityInfoState = MutableStateFlow<EquityInfo?>(null)
    val equityInfoFlow: StateFlow<EquityInfo?> = _equityInfoState.asStateFlow()

    fun updateCurrentEquityInfo(index: Int) {
        viewModelScope.launch {
            // collect 是一个挂起函数，它会持续监听 Flow 的数据流
            equityInfoListFlow.collect { equityList ->
                if (index in equityList.indices) {
                    _equityInfoState.value = equityList[index]
                }
            }
        }
    }

    // 水平滑动事件
    fun swipeScreenEquity(offset: Int) {
        viewModelScope.launch {
            val currentEquityInfo = _equityInfoState.value
            val equityInfoList = _equityInfoListState.value // Get current list

            if (currentEquityInfo != null) {
                val currentIndex = equityInfoList.indexOf(currentEquityInfo)
                val nextIndex = currentIndex + offset  // 使用 offset

                if (nextIndex in equityInfoList.indices) {
                    _equityInfoState.value = equityInfoList[nextIndex]
                } else {
                    _equityInfoState.value = null // Or handle boundary condition as needed
                }
            }

            if (_equityInfoState.value == null)  {
                if(offset >= 0 ){
                    //如果当前equityInfo是null，你想设置成列表第一个元素吗？
                    _equityInfoState.value = equityInfoList.firstOrNull()
                } else {
                    _equityInfoState.value = equityInfoList.lastOrNull()
                }
            }

            // 同时更新equity
            _equityInfoState.value?.let {_equityState.value = companyRepository.findEquityBySymbol(it.symbol).first() }
        }
    }



    fun filterEquities(any: List<Any>) {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityInfoListState.value = companyRepository.filterEquities(any).first().map{ it.toEquityInfo() }  // 使用扩展函数进行转换
        }
    }

    fun findEquityBySymbolOrName(codeOrName: String) {
        // 启动一个协程（Coroutine）
        viewModelScope.launch {
            _equityInfoListState.value = companyRepository.findEquityBySymbolOrName(codeOrName).first().map{ it.toEquityInfo() } // 使用扩展函数进行转换
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