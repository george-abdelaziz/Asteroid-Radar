package com.example.android.asteroidradar.all_fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.asteroidradar.R
import com.example.android.asteroidradar.api.AsteroidRepository
import com.example.android.asteroidradar.api.NasaApi
import com.example.android.asteroidradar.api.NasaApiServices
import com.example.android.asteroidradar.database.AsteroidDatabase
import com.example.android.asteroidradar.database.PictureDatabase
import com.example.android.asteroidradar.databinding.FragmentAllBinding
import com.example.android.asteroidradar.recyclerview.AsteroidRecyclerViewAdapter
import com.example.android.asteroidradar.recyclerview.ItemListener

class ListFragment : Fragment() {

    lateinit var viewModel: ListViewModel
    lateinit var adapter: AsteroidRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAllBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_all, container, false
        )
        val application = requireNotNull(this.activity).application
        val database = AsteroidDatabase.getInstance(application).asteroidDaoRepository
        val picDatabse = PictureDatabase.getInstance(application).pictureDaoRepository
        val retrofitService: NasaApiServices = NasaApi.retrofitService
        val reprository = AsteroidRepository(picDatabse, database, retrofitService)
        val viewModelFactory = ListViewModelFactory(picDatabse, database, reprository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ListViewModel::class.java)

        setHasOptionsMenu(true)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //var pico=PictureOfDay(media_type = "image",url="https://apod.nasa.gov/apod/image/2210/M31Clouds_Fryhover_960.jpg")

        adapter =
            AsteroidRecyclerViewAdapter(viewModel.picture, viewModel.asteroidList, ItemListener {
                viewModel.onItemClicked(it)
            })

        binding.idReAsteroid.adapter = adapter

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            if (viewModel.picture.value != null) {
                adapter.addHeaderAndSubmitList(
                    listOf(viewModel.picture.value).first(), viewModel.asteroidList.value!!.toList()
                )
            }
        })
        viewModel.picture.observe(viewLifecycleOwner, Observer {
            if (viewModel.asteroidList.value != null) {
                adapter.addHeaderAndSubmitList(
                    listOf(viewModel.picture.value).first(), viewModel.asteroidList.value!!.toList()
                )
            }
        })
        viewModel.clickedItemID.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    ListFragmentDirections.actionAllFragmentToDetailFragment(it)
                )
                viewModel.onNavigation()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(androidx.navigation.ui.R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.today_asteroid_menu ->
                if (viewModel.picture.value != null && viewModel.asteroidList1 != null) {
                    adapter.addHeaderAndSubmitList(
                        listOf(viewModel.picture.value).first(),
                        viewModel.asteroidList1!!
                    )
                }

            R.id.week_asteroid_menu ->
                if (viewModel.picture.value != null && viewModel.asteroidList2 != null) {
                    adapter.addHeaderAndSubmitList(
                        listOf(viewModel.picture.value).first(),
                        viewModel.asteroidList2!!
                    )
                }

            else ->
                if (viewModel.picture.value != null && viewModel.asteroidList.value != null) {
                    adapter.addHeaderAndSubmitList(
                        listOf(viewModel.picture.value).first(),
                        viewModel.asteroidList.value!!.toList()
                    )
                }
        }
        return true
    }
}
