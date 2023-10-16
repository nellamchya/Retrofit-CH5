package com.shine.retrofit.presentation.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.shine.retrofit.core.ViewHolderBinder
import com.shine.retrofit.data.model.Product
import com.shine.retrofit.databinding.ItemProductBinding

class ProductListAdapter: RecyclerView.Adapter<ProductItemListViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        })


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemListViewHolder {
        return ProductItemListViewHolder(
            binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ProductItemListViewHolder, position: Int) {
        holder.bind(dataDiffer.currentList[position])
    }

    fun setData(data: List<Product>) {
        dataDiffer.submitList(data)
    }

    fun refreshList() {
        notifyItemRangeChanged(0, dataDiffer.currentList.size)
    }
}


class ProductItemListViewHolder(
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Product> {
    override fun bind(product: Product) {

        with(binding) {
            ivProductImage.load(product.images[0])
            tvProductName.text = product.title
            tvProductDesc.text = product.desc
            tvProductPrice.text = "IDR ${product.price}"

        }
    }
}