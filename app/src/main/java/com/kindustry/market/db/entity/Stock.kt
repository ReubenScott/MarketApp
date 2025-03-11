package com.kindustry.market.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 定义实体类
 * 默认使用类名作为表名，可以指定
 */
@Entity(tableName = "company_statistics")
data class Stock(
    @PrimaryKey
    @ColumnInfo(name = "symbol")
    val symbol                    : String, //  コード

    @ColumnInfo(name = "name")
    val name                      : String?, //  銘柄名

    @ColumnInfo(name = "exchange")
    val exchange                  : String?, //  市場区分

    @ColumnInfo(name = "established_date")
    val establishedDate        :  String?,      // 設立日

    @ColumnInfo(name = "listing_date")
    val ListingDate            :  String?,      // 上場日

    @ColumnInfo(name = "sector")
    val sector                  :  String?,      // 東証業種名 業種

    @ColumnInfo(name = "industry")
    val industry                  : String?, //  日経業種分類 業界

    @ColumnInfo(name = "dividend_yield")
    val dividendYield          :  Float?,      // 配当利回り

    @ColumnInfo(name = "ex_dividend_date")
    val exDividendDate        :  String?,      // 除息日

    @ColumnInfo(name = "year_change_ratio")
    val yearChangeRatio       :  Float?,      // 年初来株価上昇率

    @ColumnInfo(name = "present_price")
    val presentPrice           :  Float?,      // 現在株価

    @ColumnInfo(name = "book_value_per_share")
    val bookValuePerShare    :  Float?,      // 1株純資産

    @ColumnInfo(name = "year_low")
    val yearLow                :  Float?,      // 年初来安値

    @ColumnInfo(name = "year_high")
    val yearHigh               :  Float?,      // 年初来高値

    @ColumnInfo(name = "moving_average")
    val movingAverage          :  Float?,      // 200日移動平均線

    @ColumnInfo(name = "volume")
    val volume                  :  Int?,      // 出来高

    @ColumnInfo(name = "per")
    val per                     :  Float?,      // 株価収益率

    @ColumnInfo(name = "pbr")
    val pbr                     : Float?,      // 株価純資産倍率

    @ColumnInfo(name = "ev_revenue")
    val evRevenue              :  Float?,      // 企业价值/收入

    @ColumnInfo(name = "ev_ebitda")
    val evEbitda               :  Float?,      // 企业价值/息税前利润

    @ColumnInfo(name = "eps")
    val eps                     :  Float?,      // 基本1株当たり利益

    @ColumnInfo(name = "roa")
    val roa                     :  Float?,      // 総資産利益率

    @ColumnInfo(name = "roe")
    val roe                     :  Float?,      // 株主資本利益率

    @ColumnInfo(name = "debt_equity_ratio")
    val debtEquityRatio       :  Float?,      // 债务权益比率

    @ColumnInfo(name = "own_capital_ratio")
    val ownCapitalRatio       :  Float?,      // 自己資本比率

    @ColumnInfo(name = "market_cap")
    val marketCap              :  Float?,      // 時価総額

    @ColumnInfo(name = "enterprise_value")
    val enterpriseValue        :  Float?,      // 企業価値

    @ColumnInfo(name = "credit_multiplier")
    val creditMultiplier       :  Float?,      // 信用倍率

    @ColumnInfo(name = "grade_rating")
    val gradeRating            :  Float?,      // レーティング

    @ColumnInfo(name = "index_adoption")
    val indexAdoption          :  String?,      // 指数採用

    @ColumnInfo(name = "per_unit")
    val perUnit                :  String?,      // 単元株数

    @ColumnInfo(name = "issued_shares")
    val issuedShares           :  Int?,      // 発行済株数

    @ColumnInfo(name = "business_scope")
    val businessScope          :  String?,      // 事業内容

    @ColumnInfo(name = "product_range")
    val productRange           :  String?,      // 取扱い商品

    @ColumnInfo(name = "representative")
    val representative          :  String?,      // 代表者

    @ColumnInfo(name = "capital_stock")
    val capitalStock           :  String?,      // 資本金

    @ColumnInfo(name = "address")
    val address                 :  String?,      // 本社住所

    @ColumnInfo(name = "tel")
    val tel                     :  String?,      // 電話番号

    @ColumnInfo(name = "url")
    val url                     :  String?,      // URL

    @ColumnInfo(name = "amount_of_sales")
    val amountOfSales           : String?, //  売上高

    @ColumnInfo(name = "net_income")
    val netIncome                : String?, //  当期純利益

    @ColumnInfo(name = "sales_cf")
    val salesCf                  : String?, //  営業C/F

    @ColumnInfo(name = "total_assets")
    val totalAssets              : String?, //  総資産

    @ColumnInfo(name = "cash_and_deposits")
    val cashAndDeposits         : String?, //  現預金等

    @ColumnInfo(name = "total_capital")
    val totalCapital             : String?, //  資本合計

    @ColumnInfo(name = "average_annual_income")
    val averageAnnualIncome     : String?, //  平均年収

    @ColumnInfo(name = "delisting_date")
    val delistingDate          :  String?,      // 上場廃止日

    @ColumnInfo(name = "update_date")
    val updateDate             :  String?,      // 更新日

)
{
//    constructor(a:String,p:Float,p2:Int,n:String):this(){
//        this.author=a
//        this.price=p
//        this.pages=p2
//        this.name=n
//    }

}