package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.Device
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: Device)

    @Query("DELETE FROM DID_DEVICE_INFO")
    suspend fun deleteAll()

    @Transaction
    suspend fun sync(device: Device) {
        deleteAll()
        insert(device)
    }

    @Query(
        """
            SELECT DEVICE_ID, DEVICE_NO, CMP_CD, SALES_ORG_CD, STOR_CD, CORNER_CD, DISPLAY_MENU_CD, ROLLING_YN, USE_YN
            FROM DID_DEVICE_INFO
            WHERE 1 = 1
            AND DEVICE_ID = :deviceId
        """
    )
    fun get(deviceId: String): Flow<Device>
}