package com.example.halal_app.productcheck

import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.halal_app.productcheck.network.Product
import com.example.halal_app.productcheck.network.ProductApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.properties.Delegates

class  ProductCheckViewModel : ViewModel() {

    //EAN Code
    private val _ean = MutableLiveData<String>()
    val ean: LiveData<String>
        get() = _ean

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _ingredients = MutableLiveData<String>()
    val ingredients: LiveData<String>
        get() = _ingredients

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product>
        get() = _product

    private val _isLoaded = MutableLiveData<Boolean>()
    val isLoaded: LiveData<Boolean>
        get() = _isLoaded

    init {
        _title.value = ""
        _imageUrl.value = ""
        _status.value = ""
        _ingredients.value = ""
    }

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main)

    fun setEan(number: String){
        _isLoaded.value = false
        _ean.value = number
        getProductInfo()
    }

    private fun getProductInfo(){
        coroutineScope.launch {
            val getProduct = ProductApi.retrofitService.getProductAsync(ean.value)
            try {
                val productResult = getProduct.await()
                _title.value = productResult.title
                _imageUrl.value = productResult.imageUrl
                _status.value = productResult.status
                _ingredients.value = productResult.ingredient
                _isLoaded.value = true
            }catch (e: Exception){
                _title.value = "Product not found: \nPlease enter an EAN Code or check the correctness of EAN Code"
                _imageUrl.value = "none"
                _status.value = "none"
                _ingredients.value = "none"
                _isLoaded.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

