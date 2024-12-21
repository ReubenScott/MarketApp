package com.kindustry.market.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module// provide instance of certain type
@InstallIn(SingletonComponent::class)// inform in which Android class each module will be used or replaced
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        MarketDatabase::class.java,
        "market"
    ).createFromAsset("database/market.db").build()

    @Provides
    @Singleton
    fun provideDao(db: MarketDatabase) = db.stockDao
}