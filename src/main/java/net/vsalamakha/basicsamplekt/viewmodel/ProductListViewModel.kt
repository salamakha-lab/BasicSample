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

package net.vsalamakha.basicsamplekt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import net.vsalamakha.basicsamplekt.BasicApp
import net.vsalamakha.basicsamplekt.DataRepository
import net.vsalamakha.basicsamplekt.db.entity.ProductEntity

class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: DataRepository
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private val mObservableProducts: MediatorLiveData<List<ProductEntity>> = MediatorLiveData()

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    val products: LiveData<List<ProductEntity>>
        get() = mObservableProducts

    init {

        // set by default null, until we get data from the database.
        mObservableProducts.value = null

        mRepository = (application as BasicApp).repository
        val products = mRepository.products

        // observe the changes of the products from the database and forward them
        mObservableProducts.addSource<List<ProductEntity>>(products) {
            mObservableProducts.setValue(it)
        }
    }

    fun searchProducts(query: String): LiveData<List<ProductEntity>> {
        return mRepository.searchProducts(query)
    }
}
