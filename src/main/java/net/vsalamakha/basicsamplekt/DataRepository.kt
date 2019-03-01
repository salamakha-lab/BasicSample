package net.vsalamakha.basicsamplekt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import net.vsalamakha.basicsamplekt.db.AppDatabase
import net.vsalamakha.basicsamplekt.db.entity.CommentEntity
import net.vsalamakha.basicsamplekt.db.entity.ProductEntity

/**
 * Repository handling the work with products and comments.
 */
class DataRepository private constructor(private val mDatabase: AppDatabase) {

    private val mObservableProducts: MediatorLiveData<List<ProductEntity>> = MediatorLiveData()
    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    val products: LiveData<List<ProductEntity>>
        get() = mObservableProducts

    init {

        mObservableProducts.addSource(mDatabase.productDao().loadAllProducts()) { productEntities ->
            if (mDatabase.databaseCreated.value  != null) {
                mObservableProducts.postValue(productEntities)
            }
        }
    }

    fun loadProduct(productId: Int): LiveData<ProductEntity> {
        return mDatabase.productDao().loadProduct(productId)
    }

    fun loadComments(productId: Int): LiveData<List<CommentEntity>> {
        return mDatabase.commentDao().loadComments(productId)
    }

    fun searchProducts(query: String): LiveData<List<ProductEntity>> {
        return mDatabase.productDao().searchAllProducts(query)
    }

    companion object {

        private var sInstance: DataRepository? = null

        fun getInstance(database: AppDatabase): DataRepository {
            if (sInstance == null) {
                synchronized(DataRepository::class.java) {
                    if (sInstance == null) {
                        sInstance = DataRepository(database)
                    }
                }
            }
            return sInstance!!
        }
    }
}
