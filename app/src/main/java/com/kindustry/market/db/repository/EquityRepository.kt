package com.kindustry.market.db.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kindustry.market.db.dao.EquityDao
import com.kindustry.market.db.entity.Equity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    val equityDao: EquityDao
) {
    val allExchange: Flow<List<String>> = equityDao.getAllExchange()

    val allSector: Flow<List<String>> = equityDao.getAllSector()

    val randomEquitys: Flow<List<Equity>> = equityDao.getRandomEquity()
    fun findEquityBySymbol(symbol: String): Flow<Equity> = equityDao.getEquityBySymbol(symbol)

    fun findEquityBySymbolOrName(codeOrName: String): Flow<List<Equity>>  = equityDao.findEquityBySymbolOrName(codeOrName)

    fun filterEquities(any: List<Any>): Flow<List<Equity>> {

        val exchange = any.getOrNull(0) as? String // 安全转换为 String
        val sector = any.getOrNull(1) as? String // 安全转换为 String
        val yearChangeRange = any.getOrNull(2) as? String // 上昇率
        val movingAverageRange = any.getOrNull(3) as? String // 乖離率
        val debtAssetRange = any.getOrNull(4) as? String // 負債率
        val perRange = any.getOrNull(5) as? String // PER
        val pbrRange = any.getOrNull(6) as? String // PBR
        val dividendYieldRange = any.getOrNull(7) as? String // 股息

        val parameters = mutableListOf<Any?>() // 创建一个可变列表来存储参数

        val queryBuilder = StringBuilder("SELECT * FROM equity_statistics WHERE 1=1") // 初始条件，确保后续 AND 语句正确

        if (exchange?.isBlank() == false) {
            queryBuilder.append(" AND exchange = :exchange")
            parameters.add(exchange) // 将参数添加到列表中
        }

        if (sector?.isBlank() == false) {
            queryBuilder.append(" AND sector = :sector")
            parameters.add(sector) // 将参数添加到列表中
        }

        // 年初来株価上昇率
        addQueryRangeCondition("year_change_ratio", yearChangeRange, queryBuilder , parameters)

        // 200日移動平均乖離率
        addQueryRangeCondition("(present_price - moving_average)*100 /moving_average", movingAverageRange, queryBuilder , parameters)

        // 負債比率 debtEquityRatio から計算
        addQueryRangeCondition("IFNULL(debt_equity_ratio/(debt_equity_ratio + 100), 0)", debtAssetRange, queryBuilder , parameters)

        // PER 株価収益率
        addQueryRangeCondition("per", perRange, queryBuilder , parameters)

        // PBR 株価純資産倍率
        addQueryRangeCondition("pbr", pbrRange, queryBuilder , parameters)

        // 股息
        addQueryRangeCondition("dividend_yield", dividendYieldRange, queryBuilder , parameters)

        queryBuilder.append(" AND delisting_date is NULL")  // 上場廃止日

        return equityDao.getEquityList(SimpleSQLiteQuery(queryBuilder.toString(), parameters.toTypedArray()))
    }


    private fun addQueryRangeCondition(conditionColumn: String?, conditionRange: String?, queryBuilder: StringBuilder, parameters: MutableList<Any?>) {
        if (conditionRange?.isBlank() == false) {
            val range = conditionRange.split(",").map { it }
            val min = range.getOrNull(0)
            val max = range.getOrNull(1)

            if (min?.isNotBlank() == true) {
                queryBuilder.append(" AND $conditionColumn > ?")
                parameters.add(min.toFloat())
            }

            if (max?.isNotBlank() == true) {
                queryBuilder.append(" AND $conditionColumn <= ?")
                parameters.add(max.toFloat())
            }
        }
    }

}