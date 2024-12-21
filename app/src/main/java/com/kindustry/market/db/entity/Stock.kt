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
    val established_date        :  String?,      // 設立日

    @ColumnInfo(name = "listing_date")
    val listing_date            :  String?,      // 上場日

    @ColumnInfo(name = "sector")
    val sector                  :  String?,      // 東証業種名 業種

    @ColumnInfo(name = "industry")
    val industry                  : String?, //  日経業種分類 業界

    @ColumnInfo(name = "dividend_yield")
    val dividend_yield          :  Float?,      // 配当利回り

    @ColumnInfo(name = "ex_dividend_date")
    val ex_dividend_date        :  String?,      // 除息日

    @ColumnInfo(name = "year_change_ratio")
    val year_change_ratio       :  Float?,      // 年初来株価上昇率

    @ColumnInfo(name = "present_price")
    val present_price           :  Float?,      // 現在株価

    @ColumnInfo(name = "book_value_per_share")
    val book_value_per_share    :  Float?,      // 1株純資産

    @ColumnInfo(name = "year_low")
    val year_low                :  Float?,      // 年初来安値

    @ColumnInfo(name = "year_high")
    val year_high               :  Float?,      // 年初来高値

    @ColumnInfo(name = "moving_average")
    val moving_average          :  Float?,      // 200日移動平均線

    @ColumnInfo(name = "volume")
    val volume                  :  Int?,      // 出来高

    @ColumnInfo(name = "per")
    val per                     :  Float?,      // 株価収益率

    @ColumnInfo(name = "pbr")
    val pbr                     : Float?,      // 株価純資産倍率

    @ColumnInfo(name = "ev_revenue")
    val ev_revenue              :  Float?,      // 企业价值/收入

    @ColumnInfo(name = "ev_ebitda")
    val ev_ebitda               :  Float?,      // 企业价值/息税前利润

    @ColumnInfo(name = "eps")
    val eps                     :  Float?,      // 基本1株当たり利益

    @ColumnInfo(name = "roa")
    val roa                     :  Float?,      // 総資産利益率

    @ColumnInfo(name = "roe")
    val roe                     :  Float?,      // 株主資本利益率

    @ColumnInfo(name = "debt_equity_ratio")
    val debt_equity_ratio       :  Float?,      // 债务权益比率

    @ColumnInfo(name = "own_capital_ratio")
    val own_capital_ratio       :  Float?,      // 自己資本比率

    @ColumnInfo(name = "market_cap")
    val market_cap              :  Float?,      // 時価総額

    @ColumnInfo(name = "enterprise_value")
    val enterprise_value        :  Float?,      // 企業価値

    @ColumnInfo(name = "credit_multiplier")
    val credit_multiplier       :  Float?,      // 信用倍率

    @ColumnInfo(name = "grade_rating")
    val grade_rating            :  Float?,      // レーティング

    @ColumnInfo(name = "index_adoption")
    val index_adoption          :  String?,      // 指数採用

    @ColumnInfo(name = "per_unit")
    val per_unit                :  String?,      // 単元株数

    @ColumnInfo(name = "issued_shares")
    val issued_shares           :  Int?,      // 発行済株数

    @ColumnInfo(name = "business_scope")
    val business_scope          :  String?,      // 事業内容

    @ColumnInfo(name = "product_range")
    val product_range           :  String?,      // 取扱い商品

    @ColumnInfo(name = "representative")
    val representative          :  String?,      // 代表者

    @ColumnInfo(name = "capital_stock")
    val capital_stock           :  String?,      // 資本金

    @ColumnInfo(name = "address")
    val address                 :  String?,      // 本社住所

    @ColumnInfo(name = "tel")
    val tel                     :  String?,      // 電話番号

    @ColumnInfo(name = "url")
    val url                     :  String?,      // URL

    @ColumnInfo(name = "amount_of_sales")
    val amount_of_sales           : String?, //  売上高

    @ColumnInfo(name = "net_income")
    val net_income                : String?, //  当期純利益

    @ColumnInfo(name = "sales_cf")
    val sales_cf                  : String?, //  営業C/F

    @ColumnInfo(name = "total_assets")
    val total_assets              : String?, //  総資産

    @ColumnInfo(name = "cash_and_deposits")
    val cash_and_deposits         : String?, //  現預金等

    @ColumnInfo(name = "total_capital")
    val total_capital             : String?, //  資本合計

    @ColumnInfo(name = "average_annual_income")
    val average_annual_income     : String?, //  平均年収

    @ColumnInfo(name = "delisting_date")
    val delisting_date          :  String?,      // 上場廃止日

    @ColumnInfo(name = "update_date")
    val update_date             :  String?,      // 更新日

)
{
//    constructor(a:String,p:Float,p2:Int,n:String):this(){
//        this.author=a
//        this.price=p
//        this.pages=p2
//        this.name=n
//    }

}