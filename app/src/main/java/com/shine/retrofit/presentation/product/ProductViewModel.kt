package com.shine.retrofit.presentation.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shine.retrofit.data.model.ProductsResponse
import com.shine.retrofit.data.repository.ProductRepository
import com.shine.retrofit.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepo: ProductRepository
) : ViewModel(){

    val productList = MutableLiveData<ResultWrapper<ProductsResponse>>()

    fun getProductList(){
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.getProducts().collect{
                productList.postValue(it)
            }
        }
    }

}