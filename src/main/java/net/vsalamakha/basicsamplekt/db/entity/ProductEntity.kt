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

package net.vsalamakha.basicsamplekt.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.vsalamakha.basicsamplekt.model.Product


@Entity(tableName = "products")
data class ProductEntity (
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,
    override var name: String? = null,
    override var description: String? = null,
    override var price: Int = 0
): Product

