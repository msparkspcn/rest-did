package com.secta9ine.rest.did.util

import android.util.Log
import com.secta9ine.rest.did.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.json.JSONArray
import javax.inject.Inject

class SoldOutUpdater @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend fun update(data: String) {
        try {
            val jsonArray = JSONArray(data)

            coroutineScope {
                val updateJobs = (0 until jsonArray.length()).map { index ->
                    async(Dispatchers.IO) {
                        val jsonObject = jsonArray.getJSONObject(index)
                        val cmpCd = jsonObject.getString("cmpCd")
                        val salesOrgCd = jsonObject.getString("salesOrgCd")
                        val storCd = jsonObject.getString("storCd")
                        val itemCd = jsonObject.getString("itemCd")
                        val soldoutYn = jsonObject.getString("soldoutYn")

                        productRepository.updateSoldoutYn(
                            cmpCd, salesOrgCd, storCd, itemCd, soldoutYn
                        )
                    }
                }

                updateJobs.awaitAll()
            }
        } catch (e: Exception) {
            Log.e("ProductSoldOutUpdater", "품절 처리 중 오류 발생", e)
        }
    }
}
