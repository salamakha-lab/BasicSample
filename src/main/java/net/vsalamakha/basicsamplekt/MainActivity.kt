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

package net.vsalamakha.basicsamplekt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.vsalamakha.basicsamplekt.model.Product
import net.vsalamakha.basicsamplekt.ui.ProductFragment
import net.vsalamakha.basicsamplekt.ui.ProductListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            val fragment = ProductListFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment,
                    ProductListFragment.TAG
                ).commit()
        }
    }

    /** Shows the product detail fragment  */
    fun show(product: Product) {

        //val productFragment = ProductFragment.forProduct(product.id)

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("product")
            .replace(
                R.id.fragment_container,
                ProductFragment.forProduct(product.id),//productFragment,
                null
            ).commit()
    }
}
