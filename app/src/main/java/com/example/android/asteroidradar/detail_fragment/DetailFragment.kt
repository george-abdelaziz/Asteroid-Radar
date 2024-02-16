package com.example.android.asteroidradar.detail_fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.asteroidradar.R
import com.example.android.asteroidradar.database.AsteroidDatabase
import com.example.android.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )
        val arguments = DetailFragmentArgs.fromBundle(requireArguments())
        val application = requireNotNull(this.activity).application
        val databaseRef = AsteroidDatabase.getInstance(application).asteroidDaoRepository
        val viewModelFactory = DetailViewModelFactory(databaseRef, application, arguments.id)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DetailViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.helpCircleImage.setOnClickListener {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it, R.style.AlertDialogCustom)
            }
            //builder?.setCancelable(false)
            builder?.setMessage(R.string.astronomica_unit_explanation)
                ?.setTitle(R.string.astronomical_unit_explanation_button)
            builder?.setPositiveButton(R.string.ac,
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
            val dialog: AlertDialog? = builder?.create()
            dialog?.window?.setBackgroundDrawableResource(R.color.white)
            //dialog?.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.potentially_hazardous))
            dialog?.show()
        }
        return binding.root
    }
}
