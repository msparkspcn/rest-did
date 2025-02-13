//package com.secta9ine.rest.did.data.local.database
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.secta9ine.rest.did.data.local.dao.OrderStatusDao
//import com.secta9ine.rest.did.data.local.dao.ProductDao
//import com.secta9ine.rest.did.data.local.dao.StoreDao
//import com.secta9ine.rest.did.domain.model.OrderStatus
//import com.secta9ine.rest.did.domain.model.Product
//import com.secta9ine.rest.did.domain.model.Store
//
//@Database(
//    entities = [
//        Product::class,
//        Store::class,
//        OrderStatus::class
//    ],
//    version = 1,
//    exportSchema = true,
//)
//abstract class AppDatabase : RoomDatabase() {
//    abstract val storeDao: StoreDao
//    abstract val productDao: ProductDao
//    abstract val orderStatusDao: OrderStatusDao
//}