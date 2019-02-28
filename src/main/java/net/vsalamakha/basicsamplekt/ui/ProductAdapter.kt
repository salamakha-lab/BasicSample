/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.vsalamakha.basicsamplekt.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.vsalamakha.basicsamplekt.R
import net.vsalamakha.basicsamplekt.databinding.ProductItemBinding
import net.vsalamakha.basicsamplekt.db.entity.ProductEntity

class ProductAdapter(private val mProductClickCallback: ProductClickCallback?) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var mProductList: List<ProductEntity>? = null

    init {
        setHasStableIds(true)
    }

    fun setProductList(productList: List<ProductEntity>) {
        when (mProductList) {
            null -> {
                mProductList = productList
                notifyItemRangeInserted(0, productList.size)
            }
            else -> {
                val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int {
                        return mProductList!!.size
                    }

                    override fun getNewListSize(): Int {
                        return productList.size
                    }

                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return (mProductList!![oldItemPosition].id/*getId()*/ ==
                                productList.get(newItemPosition).id)//getId()
                    }

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        val newProduct = productList[newItemPosition]
                        val oldProduct = mProductList!![oldItemPosition]
                        return (newProduct.id == oldProduct.id
                                && newProduct.description == oldProduct.description
                                && newProduct.name == oldProduct.name
                                && newProduct.price == oldProduct.price)
                    }
                })
                mProductList = productList
                result.dispatchUpdatesTo(this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(DataBindingUtil.inflate<ProductItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.product_item,
            parent,
            false
        ).also{
            it.callback = mProductClickCallback
        })
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.product = mProductList!![position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return when (mProductList) {
            null -> 0
            else -> mProductList!!.size
        }
    }

    override fun getItemId(position: Int): Long {
        return mProductList!![position].id.toLong()
    }

    class ProductViewHolder(val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)
}