package com.kindustry.market.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module// provide instance of certain type
@InstallIn(SingletonComponent::class)// inform in which Android class each module will be used or replaced
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ) : MarketDatabase {
        // 在应用启动时，您可以检查外部文件的修改时间，并与应用私有数据目录下的数据库文件进行比较。
        val externalDbFile = File("/storage/emulated/0/Studio/Data/market.db")
        val internalDbFile = context.getDatabasePath("market")

        if (externalDbFile.exists()) { // 检查外部文件是否存在
            // if (externalDbFile.lastModified() > internalDbFile.lastModified())
            // 清理/data目录下数据库，数据文件3套件 market， market-wal， market-shm
            // /data/user/0/com.kindustry.market/databases
//            internalDbFile.parentFile?.deleteRecursively()
            // 复制外部数据库文件到内部存储
            //  externalDbFile.copyTo(internalDbFile, overwrite = true)

            // 从外部文件，创建新的Room数据库实例
            return Room.databaseBuilder(
                context,
                MarketDatabase::class.java,
                "market"
            ).fallbackToDestructiveMigration()  // 强制关闭旧的数据库实例
             .createFromFile(externalDbFile)    // 从外部文件创建数据库
             .build()
        } else {
            // 使用APK发布包中assets目錄下的檔案，创建Room数据库实例
            return Room.databaseBuilder(
                context,
                MarketDatabase::class.java,
                "market"
            ).fallbackToDestructiveMigration()
            .createFromAsset("database/market.db") //  只能存取assets目錄下的檔案
            .build()
        }
    }

    @Provides
    @Singleton
    fun provideDao(db: MarketDatabase) = db.equityDao
}