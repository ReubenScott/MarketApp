package com.kindustry.market.db.repository

import com.kindustry.market.db.dao.StockDao
import com.kindustry.market.db.entity.Stock
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    val stockDao: StockDao
) {
    val readAll = stockDao.getAllStock()

    val randomStocks: Flow<List<Stock>> = stockDao.getRandomStock()
}