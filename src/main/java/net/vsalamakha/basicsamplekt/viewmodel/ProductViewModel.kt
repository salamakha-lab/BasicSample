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
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.vsalamakha.basicsamplekt.BasicApp
import net.vsalamakha.basicsamplekt.DataRepository
import net.vsalamakha.basicsamplekt.db.entity.CommentEntity
import net.vsalamakha.basicsamplekt.db.entity.ProductEntity

class ProductViewModel(
    application: Application,
    repository: DataRepository,
    mProductId: Int
) : AndroidViewModel(application) {

    val observableProduct: LiveData<ProductEntity> = repository.loadProduct(mProductId)
    val product: ObservableField<ProductEntity> = ObservableField()

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    val comments: LiveData<List<CommentEntity>> = repository.loadComments(mProductId)

    fun setProduct(product: ProductEntity) {
        this.product.set(product)
    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     *
     *
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    class Factory(private val mApplication: Application, private val mProductId: Int) :
        ViewModelProvider.NewInstanceFactory() {

        private val mRepository: DataRepository = (mApplication as BasicApp).repository

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ProductViewModel(mApplication, mRepository, mProductId) as T
        }
    }
}
