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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import net.vsalamakha.basicsamplekt.MainActivity
import net.vsalamakha.basicsamplekt.R
import net.vsalamakha.basicsamplekt.databinding.ListFragmentBinding
import net.vsalamakha.basicsamplekt.db.entity.ProductEntity
import net.vsalamakha.basicsamplekt.model.Product
import net.vsalamakha.basicsamplekt.viewmodel.ProductListViewModel

class ProductListFragment : Fragment() {

    private var mProductAdapter: ProductAdapter? = null
    private var mBinding: ListFragmentBinding? = null

    private val mProductClickCallback = object : ProductClickCallback() {
        override fun onClick(product: Product) {

            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                (activity as MainActivity).show(product)
            }
        }
    }

    override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding  = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)
        mProductAdapter = ProductAdapter(mProductClickCallback)
        mBinding!!.productsList.adapter = mProductAdapter

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)

        mBinding!!.productsSearchBtn.setOnClickListener {
            val query = mBinding!!.productsSearchBox.text
            if (query == null || query.toString().isEmpty()) {
                subscribeUi(viewModel.products)
            } else subscribeUi(viewModel.searchProducts("*$query*"))
        }
        subscribeUi(viewModel.products)
    }

    private fun subscribeUi(liveData: LiveData<List<ProductEntity>>) {
        // Update the list when the data changes
        liveData.observe(this, Observer<List<ProductEntity>> { myProducts ->
            if (myProducts != null) {
                mBinding!!.isLoading = false
                mProductAdapter!!.setProductList(myProducts)
            } else {
                mBinding!!.isLoading = true
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            mBinding!!.executePendingBindings()
        })
    }

    companion object {
        const val TAG = "ProductListFragment"
    }
}
