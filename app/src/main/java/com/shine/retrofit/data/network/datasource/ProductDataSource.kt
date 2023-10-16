package com.shine.retrofit.data.network.datasource

import com.shine.retrofit.data.model.ProductsResponse
import com.shine.retrofit.data.network.service.ProductService

interface ProductDataSource {
    suspend fun getProducts(): ProductsResponse
}

class ProductDataSourceImpl(private val service: ProductService) : ProductDataSource {
    override suspend fun getProducts(): ProductsResponse {
        return service.getProducts()
    }
}