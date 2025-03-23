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

    fun getQueryEquitys(symbol: String): Flow<Equity> = equityDao.selectNoteID(symbol)

//    fun getQueryEquitys(exchange: String?, sector: String?): Flow<List<Equity>> {
      fun getQueryEquitys(any: List<Any>): Flow<List<Equity>> {

        // exchange: String?, sector: String?
        var exchange = any.getOrNull(0) as? String // 安全转换为 String
        var sector = any.getOrNull(1) as? String // 安全转换为 String

        val parameters = mutableListOf<Any?>() // 创建一个可变列表来存储参数

        val queryBuilder = StringBuilder("SELECT * FROM equity_statistics WHERE 1=1") // 初始条件，确保后续 AND 语句正确

        if (!(exchange?.isNullOrBlank()  ?: true )) {
            queryBuilder.append(" AND exchange = :exchange")
            parameters.add(exchange) // 将参数添加到列表中
        }

        if (!(sector?.isNullOrBlank() ?: true)) {
            queryBuilder.append(" AND sector = :sector")
            parameters.add(sector) // 将参数添加到列表中
        }

        queryBuilder.append(" LIMIT 40")

        return equityDao.getEquityList(SimpleSQLiteQuery(queryBuilder.toString(), parameters.toTypedArray()))
    }

}