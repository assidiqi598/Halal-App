package com.example.halal_app.praytimes

import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.halal_app.R
import com.example.halal_app.databinding.FragmentPrayTimesBinding
import java.time.LocalTime


class PrayTimesFragment : Fragment() {

    private lateinit var binding: FragmentPrayTimesBinding

    private lateinit var viewModel: PrayTimesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pray_times,
            container,
            false
        )



        viewModel = ViewModelProvider(this).get(PrayTimesViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.prayTimesViewModel = viewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        //getColor is deprecated, use: https://stackoverflow.com/a/32202256/13862433
        DrawableCompat.setTint(
            binding.progressBar.indeterminateDrawable,
            ContextCompat.getColor(context!!, R.color.colorPrimary)
        ) //For setting the color of progress bar: https://stackoverflow.com/questions/2020882/how-to-change-progress-bars-progress-color-in-android?page=2&tab=votes#tab-top

        //binding.ashrTime.setBackgroundColor(Color.BLUE)

        //as reference : https://stackoverflow.com/questions/17823451/set-android-shape-color-programmatically
        binding.dateView.background.colorFilter = PorterDuffColorFilter(
            getColor(context!!, R.color.colorDarkPink),
            PorterDuff.Mode.SRC_IN
        ) //change dateView drawable background color

        val time = LocalTime.now()

        viewModel.isLoaded.observe(viewLifecycleOwner, Observer<Boolean> {
            if (viewModel.isLoaded.value == true) {
                binding.progressBar.visibility = View.INVISIBLE

                val fajrTime = viewModel.fajr.value.toString()
                val sunriseTime = viewModel.sunrise.value.toString()
                val dzuhurTime = viewModel.dzuhur.value.toString()
                val ashrTime = viewModel.ashr.value.toString()
                val maghribTime = viewModel.maghrib.value.toString()
                val isyaTime = viewModel.isya.value.toString()

                val fajrSplit = fajrTime.split(":") //well, I considered to use split after saw this: https://stackoverflow.com/questions/27643810/java-localdatetime-parse-error
                val sunriseSplit = sunriseTime.split(":")
                val dzuhurSplit = dzuhurTime.split(":")
                val ashrSplit = ashrTime.split(":")
                val maghribSplit = maghribTime.split(":")
                val isyaSplit = isyaTime.split(":")

                val fajr = LocalTime.of(fajrSplit[0].toInt(), fajrSplit[1].toInt())
                val sunrise = LocalTime.of(sunriseSplit[0].toInt(), sunriseSplit[1].toInt())
                val dzuhur = LocalTime.of(dzuhurSplit[0].toInt(), dzuhurSplit[1].toInt())
                val ashr = LocalTime.of(ashrSplit[0].toInt(), ashrSplit[1].toInt())
                val maghrib = LocalTime.of(maghribSplit[0].toInt(), maghribSplit[1].toInt())
                val isya = LocalTime.of(isyaSplit[0].toInt(), isyaSplit[1].toInt())

                Log.d("subuh", fajr.toString())
                val timeAfterFajr = time.isAfter(fajr)
                val timeBeforeSunrise = time.isBefore(sunrise)
                if (timeAfterFajr && timeBeforeSunrise){
                    binding.fajrView.background.colorFilter = PorterDuffColorFilter(
                        getColor(context!!, R.color.colorPrimary),
                        PorterDuff.Mode.SRC_IN
                    )
                }


                val timeAfterSunrise = time.isAfter(sunrise)
                val timeBeforeDzuhur = time.isBefore(dzuhur)
                if (timeAfterSunrise && timeBeforeDzuhur){
                    binding.sunriseView.background.colorFilter = PorterDuffColorFilter(getColor(context!!, R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                }

                val timeAfterDzuhur = time.isAfter(dzuhur)
                val timeBeforeAshr = time.isBefore(ashr)
                Log.d("timeAfterDzuhur", timeAfterDzuhur.toString())
                Log.d("timeBeforeAshr", timeBeforeAshr.toString())
                Log.d("time", time.toString())
                Log.d("timeAshr", ashr.toString())
                if (timeAfterDzuhur && timeBeforeAshr){
                    binding.dzuhurView.background.colorFilter = PorterDuffColorFilter(getColor(context!!, R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                }

                val timeAfterAshr = time.isAfter(ashr)
                val timeBeforeMaghrib = time.isBefore(maghrib)
                if (timeAfterAshr && timeBeforeMaghrib){
                    binding.ashrView.background.colorFilter = PorterDuffColorFilter(getColor(context!!, R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                }

                val timeAfterMaghrib = time.isAfter(maghrib)
                val timeBeforeIsya = time.isBefore(isya)
                if (timeAfterMaghrib && timeBeforeIsya){
                    binding.maghribView.background.colorFilter = PorterDuffColorFilter(getColor(context!!, R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                }

                val timeAfterIsya = time.isAfter(isya)
                if (timeAfterIsya){
                    binding.isyaView.background.colorFilter = PorterDuffColorFilter(getColor(context!!, R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                }

            } else if (viewModel.isLoaded.value == false)
                binding.progressBar.visibility = View.VISIBLE
        })



        return binding.root
    }
}
