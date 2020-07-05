package com.example.halal_app.manual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.halal_app.R
import com.example.halal_app.databinding.ActivityMainBinding
import com.example.halal_app.databinding.FragmentManualBinding
import com.example.halal_app.praytimes.PrayTimesViewModel


class ManualFragment : Fragment() {

    private lateinit var viewModel: ManualViewModel

    private lateinit var binding: FragmentManualBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_manual,
            container,
            false
        )



        viewModel = ViewModelProvider(this).get(ManualViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.manualViewModel = viewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root

    }
}