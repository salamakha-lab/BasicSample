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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import net.vsalamakha.basicsamplekt.R
import net.vsalamakha.basicsamplekt.databinding.ProductFragmentBinding
import net.vsalamakha.basicsamplekt.db.entity.ProductEntity
import net.vsalamakha.basicsamplekt.model.Comment
import net.vsalamakha.basicsamplekt.viewmodel.ProductViewModel

class ProductFragment : Fragment() {

    private var mBinding: ProductFragmentBinding? = null
    private var mCommentAdapter: CommentAdapter? = null

    private var mCommentClickCallback = object : CommentClickCallback() {
         override fun onClick(comment: Comment) {
            // no-op
         }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.product_fragment, container, false)

        // Create and set the adapter for the RecyclerView.
        mCommentAdapter = CommentAdapter(mCommentClickCallback)
        mBinding!!.commentList.adapter = mCommentAdapter

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = ProductViewModel.Factory(
            activity!!.application, arguments!!.getInt(KEY_PRODUCT_ID)
        )

        val model = ViewModelProviders.of(this, factory)
            .get(ProductViewModel::class.java)

        mBinding!!.productViewModel = model

        subscribeToModel(model)
    }

    private fun subscribeToModel(model: ProductViewModel) {

        // Observe product data
        model.observableProduct.observe(this, Observer<Any> { productEntity -> model.setProduct(productEntity as ProductEntity)}) // .setProduct(productEntity) })

        // Observe comments
        model.comments.observe(this, Observer<List<Comment>> {
            if (it != null) {
                mBinding!!.isLoading = false
                mCommentAdapter!!.setCommentList(it as List<Comment>)
            } else {
                mBinding!!.isLoading = true
            }
        })
    }

    companion object {

        private const val KEY_PRODUCT_ID = "product_id"

        /** Creates product fragment for specific product ID  */
        fun forProduct(productId: Int): ProductFragment {
            val fragment = ProductFragment()
            val args = Bundle()
            args.putInt(KEY_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }
}

/*
base fragment
class BaseFragment: Fragment(){
    protected inline fun <T> LiveData<T>.observe(crossinline codeBlock:
    (T) -> Unit){
        observe(this@BaseFragment, Observer{
            it?.let{ codeBlock(it)}
        })
    }
}
*/
