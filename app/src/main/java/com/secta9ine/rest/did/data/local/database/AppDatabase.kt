package com.secta9ine.rest.did.data.local.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.secta9ine.rest.did.data.local.dao.DeviceDao
import com.secta9ine.rest.did.data.local.dao.OrderStatusDao
import com.secta9ine.rest.did.data.local.dao.ProductDao
import com.secta9ine.rest.did.data.local.dao.StoreDao
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.model.Stor

@Database(
    entities = [
        Product::class,
        Stor::class,
        Device::class,
        OrderStatus::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val storeDao: StoreDao
    abstract val productDao: ProductDao
    abstract val orderStatusDao: OrderStatusDao
    abstract val deviceDao: DeviceDao

    companion object {
        private const val DATABASE_NAME = "restdid.sqlite"

        fun create(app: Application): AppDatabase {
            return Room.databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}