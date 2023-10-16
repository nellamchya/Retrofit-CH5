package com.shine.retrofit.data.repository

import com.shine.retrofit.data.model.ProductsResponse
import com.shine.retrofit.data.network.datasource.ProductDataSource
import com.shine.retrofit.utils.ResultWrapper
import com.shine.retrofit.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface ProductRepository {
    suspend fun getProducts(): Flow<ResultWrapper<ProductsResponse>>
}

class ProductRepositoryImpl(private val productDataSource: ProductDataSource) : ProductRepository {
    override suspend fun getProducts(): Flow<ResultWrapper<ProductsResponse>> {
        return proceedFlow {
            productDataSource.getProducts()
        }.map {
            if (it.payload?.products?.isEmpty() == true)
                ResultWrapper.Empty(it.payload)
            else
                it
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

}