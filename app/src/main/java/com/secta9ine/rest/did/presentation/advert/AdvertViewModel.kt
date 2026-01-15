package com.secta9ine.rest.did.presentation.advert

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.model.ProductVo
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.ProductRepository
import com.secta9ine.rest.did.presentation.product.ProductViewModel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvertViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {
    private val tag = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    private val _productList = MutableStateFlow<List<ProductVo>>(emptyList())
    private val _filteredProducts = MutableStateFlow<List<ProductVo>>(emptyList())
    val advertList: StateFlow<List<ProductVo>> = _filteredProducts

    init {
        uiState.onEach { Log.d(tag, "uiState=$it") }.launchIn(viewModelScope)
//        startTimer()

        viewModelScope.launch {
            val cmpCd = dataStoreRepository.getCmpCd().first()
            val salesOrgCd = dataStoreRepository.getSalesOrgCd().first()
            val storCd = dataStoreRepository.getStorCd().first()

            productRepository.getProduct(
                cmpCd, salesOrgCd, storCd, "1100033524A001"
            ).collect { list ->
                _productList.value = list

                _filteredProducts.value = list.filter {
                    it.imgPath?.endsWith(".mp4") == true
                }

                Log.d(tag, "advert 목록=${_filteredProducts.value}")
            }
        }
    }

}