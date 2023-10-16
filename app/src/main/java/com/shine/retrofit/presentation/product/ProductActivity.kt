package com.shine.retrofit.presentation.product

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.shine.retrofit.R
import com.shine.retrofit.data.network.datasource.ProductDataSourceImpl
import com.shine.retrofit.data.network.service.ProductService
import com.shine.retrofit.data.repository.ProductRepository
import com.shine.retrofit.data.repository.ProductRepositoryImpl
import com.shine.retrofit.databinding.ActivityProductBinding
import com.shine.retrofit.utils.GenericViewModelFactory
import com.shine.retrofit.utils.proceedWhen

class ProductActivity : AppCompatActivity() {

    private val binding : ActivityProductBinding by lazy {
        ActivityProductBinding.inflate(layoutInflater)
    }

    private val adapterProduct = ProductListAdapter()

    private val viewModel : ProductViewModel by viewModels {
        val service = ProductService.invoke()
        val dataSource = ProductDataSourceImpl(service)
        val repo : ProductRepository = ProductRepositoryImpl(dataSource)
        GenericViewModelFactory.create(ProductViewModel(repo))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRvListProduct()
        observeData()
        getData()
    }

    private fun setupRvListProduct() {
        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(this@ProductActivity)
            adapter = adapterProduct
            adapterProduct.refreshList()
        }
    }

    private fun observeData() {
        viewModel.productList.observe(this){
            it.proceedWhen(
                doOnSuccess = {
                    binding.rvProduct.isVisible = true
                    binding.commonLayoutState.root.isVisible = false
                    binding.commonLayoutState.loading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = false
                    binding.rvProduct.apply {
                        layoutManager = LinearLayoutManager(this@ProductActivity)
                        adapter = adapterProduct
                    }
                    it.payload?.let {
                        adapterProduct.setData(it.products)
                    }
                },
                doOnLoading = {
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.loading.isVisible = true
                    binding.commonLayoutState.tvError.isVisible = false
                    binding.rvProduct.isVisible = false
                },
                doOnError = { err ->
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.loading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = true
                    binding.commonLayoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.rvProduct.isVisible = false
                },
                doOnEmpty = {
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.loading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = true
                    binding.commonLayoutState.tvError.text = getString(R.string.text_empty)
                    binding.rvProduct.isVisible = false
                }
            )
        }
    }

    private fun getData() {
        viewModel.getProductList()
    }

}