package com.kindustry.market.db.repository

import com.kindustry.market.db.dao.StockDao
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    private val stockDao: StockDao
) {
    val readAll = stockDao.getAllStock()
}