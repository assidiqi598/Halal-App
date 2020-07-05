package com.example.halal_app.productcheck

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.halal_app.R
import com.example.halal_app.databinding.FragmentProductCheckBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.lang.Exception
import java.net.URL


class ProductCheckFragment : Fragment() {

    private lateinit var binding: FragmentProductCheckBinding

    private lateinit var viewModel: ProductCheckViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product_check,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(ProductCheckViewModel::class.java) //https://stackoverflow.com/a/57427078

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.productCheckViewModel = viewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        DrawableCompat.setTint(binding.progressGetData.indeterminateDrawable, ContextCompat.getColor(context!!, R.color.colorPrimary)) //https://stackoverflow.com/questions/2020882/how-to-change-progress-bars-progress-color-in-android?page=2&tab=votes#tab-top

        binding.checkButton.setOnClickListener{
            viewModel.setEan(binding.eanText.text.toString())
        }

        binding.useCameraButton.setOnClickListener {
            //for using the scanner: https://www.youtube.com/watch?v=Fe7F4Jx7rwo
            val integrator = IntentIntegrator.forSupportFragment(this) //to be able to use onActivityResult in a fragment: https://github.com/journeyapps/zxing-android-embedded/issues/189
            integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13)
            integrator.setPrompt("Please focus the camera on the code")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()
        }

        viewModel.status.observe(viewLifecycleOwner, Observer<String> {
            if (viewModel.status.value == "haram")
                binding.status.setTextColor(ContextCompat.getColor(context!!, R.color.colorDarkPink))
            else if (viewModel.status.value == "hallal")
                binding.status.setTextColor(Color.GREEN)
            else
                binding.status.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

        })

        viewModel.isLoaded.observe(viewLifecycleOwner, Observer<Boolean> {
            if (viewModel.isLoaded.value == true){
                binding.progressGetData.visibility = View.INVISIBLE
                binding.title.visibility = View.VISIBLE
                binding.status.visibility = View.VISIBLE
                binding.ingredients.visibility = View.VISIBLE

                if (URLUtil.isValidUrl(viewModel.imageUrl.value)){ // check if a URL is valid: https://stackoverflow.com/questions/4905075/how-to-check-if-url-is-valid-in-android
                    //change letters in a string: https://www.techiedelight.com/replace-character-specific-index-string-kotlin/
                    Log.d("imageUrl", viewModel.imageUrl.value.toString())
                    var link = viewModel.imageUrl.value.toString()
                    val uc = "uc"
                    val index = 25
                    link = link.substring(0, 25) + uc + link.substring(index + 4)
                    Log.d("link", link)

                    try {
                        binding.productImage.visibility = View.VISIBLE
                        binding.imageUrl.visibility = View.GONE
                        Picasso.get().load(link).resize(200, 300).into(binding.productImage)
                    } catch (e: Exception){
                        binding.productImage.visibility = View.GONE
                        Toast.makeText(this.context, "Image couldn't be loaded", Toast.LENGTH_SHORT).show()
                        binding.imageUrl.visibility = View.VISIBLE
                    }
                }
            }

            else if (viewModel.isLoaded.value == false)
                binding.progressGetData.visibility = View.VISIBLE
        })


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(scanResult != null){
            if (scanResult.contents == null){
                Toast.makeText( this.context, "Cancelled", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, scanResult.contents, Toast.LENGTH_LONG).show()
                viewModel.setEan(scanResult.contents)
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.productImage.visibility = View.GONE
    }
}
