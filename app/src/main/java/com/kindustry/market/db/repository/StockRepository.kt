package com.kindustry.market.db.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kindustry.market.db.dao.StockDao
import com.kindustry.market.db.entity.Stock
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    val stockDao: StockDao
) {
    val allExchange: Flow<List<String>> = stockDao.getAllExchange()

    val allSector: Flow<List<String>> = stockDao.getAllSector()

    val randomStocks: Flow<List<Stock>> = stockDao.getRandomStock()

//    fun getQueryStocks(exchange: String, sector: String): Flow<List<Stock>> = stockDao.getStockListings(exchange, sector)

    fun getQueryStocks(exchange: String?, sector: String?): Flow<List<Stock>> {

        val parameters = mutableListOf<Any?>() // 创建一个可变列表来存储参数

        val queryBuilder = StringBuilder("SELECT * FROM company_statistics WHERE 1=1") // 初始条件，确保后续 AND 语句正确

        if (!exchange.isNullOrBlank()) {
            queryBuilder.append(" AND exchange = :exchange")
            parameters.add(exchange) // 将参数添加到列表中
        }

        if (!sector.isNullOrBlank()) {
            queryBuilder.append(" AND sector = :sector")
            parameters.add(sector) // 将参数添加到列表中
        }

        queryBuilder.append(" LIMIT 40")

        return stockDao.getStockList(SimpleSQLiteQuery(queryBuilder.toString(), parameters.toTypedArray()))
    }

}