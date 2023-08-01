package com.example.disasteralert.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disasteralert.R
import com.example.disasteralert.ViewModelFactory
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.databinding.FragmentHomeBinding
import com.example.disasteralert.helper.Constant
import com.example.disasteralert.helper.SettingPreferences
import com.example.disasteralert.helper.Util
import com.example.disasteralert.helper.Util.getAreaCode
import com.example.disasteralert.helper.Util.moveCameraAction
import com.example.disasteralert.helper.Util.placeMarkerOnMap
import com.example.disasteralert.helper.Util.setPeriodicWorkManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    lateinit var filterDialogListener: FilterFragment.OnFilterDialogListener
    private var latestFilter: String = ""

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment =
            childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val pref = SettingPreferences.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels {
            ViewModelFactory.getInstance(
                requireActivity(), pref
            )
        }
        homeViewModel = viewModel

        init()

        binding.apply {
            btnSettings.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            }

            btnFilter.setOnClickListener {
                val filterDialogFragment = FilterFragment()
                val fragmentManager = childFragmentManager
                filterDialogFragment.show(fragmentManager, FilterFragment::class.java.simpleName)
            }
        }

        filterDialogListener = object : FilterFragment.OnFilterDialogListener {
            override fun onFilterChosen(startDate: String, endDate: String) {
                if (startDate.isNotEmpty() || endDate.isNotEmpty()) {
                    binding.btnFilter.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFilter.context, R.drawable.baseline_filter_alt_off_24
                        )
                    )
                    getDisasterData(startDate = startDate, endDate = endDate)
                }
                else {
                    binding.btnFilter.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFilter.context, R.drawable.baseline_filter_alt_24
                        )
                    )
                    homeViewModel.getApiDisasterData()
                    getDisasterData()
                }
            }
        }
    }

    private fun init() {
        setBottomSheet()
        getDisasterData()
        setFilterList()
        setSearchLayout()
        getFloodGaugesData()
    }

    private fun checkTheme(viewModel: HomeViewModel) {
        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireActivity(), R.raw.map_in_night
                    )
                )
            } else {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }
    }

    private fun setBottomSheet() {
        modifyBottomSheet()
        val bottomSheet = binding.bottomSheetSection.mcvBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("Peak", slideOffset.toString())
                if (slideOffset in 0.3..0.7) bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        })
    }

    private fun modifyBottomSheet() {
        val shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
        shapeAppearanceModel.setTopLeftCorner(
            CornerFamily.ROUNDED,
            CornerSize { return@CornerSize 48F })

        shapeAppearanceModel.setTopRightCorner(
            CornerFamily.ROUNDED,
            CornerSize { return@CornerSize 48F })

        binding.bottomSheetSection.mcvBottomSheet.shapeAppearanceModel =
            shapeAppearanceModel.build()
    }

    private fun setSearchLayout() {
        val suggestionArea = ArrayList(Constant.AREA.values)
        val listAdapter = ArrayAdapter(
            requireActivity(), android.R.layout.simple_list_item_1, suggestionArea
        )
        binding.lvSuggestion.adapter = listAdapter

        binding.svSearchLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.cvSuggestion.visibility = View.GONE
                val areaKey = Constant.AREA.entries.find { it.value == query }?.key.toString()
                getDisasterData(areaKey)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    binding.cvSuggestion.visibility = View.GONE
                    getDisasterData()
                } else if (newText.length >= 3) {
                    binding.cvSuggestion.visibility = View.VISIBLE

                    listAdapter.filter.filter(newText)

                    binding.lvSuggestion.onItemClickListener =
                        AdapterView.OnItemClickListener { adapterView, view, position, id ->
                            val selectedItem = adapterView.getItemAtPosition(position) as String
                            binding.svSearchLocation.setQuery(selectedItem, true)
                            binding.cvSuggestion.visibility = View.GONE
                            val areaKey = getAreaCode(selectedItem)
                            if (latestFilter.isNotEmpty()) getDisasterData(
                                locFilter = areaKey,
                                disasterFilter = latestFilter
                            )
                            else getDisasterData(locFilter = areaKey)

                        }
                }
                return false
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        mMap.setPadding(0, 0, 0, 150)

        checkTheme(homeViewModel)
    }

    override fun onMarkerClick(p0: Marker): Boolean = false

    private fun getDisasterData(
        locFilter: String = "",
        disasterFilter: String = "",
        startDate: String = "",
        endDate: String = ""
    ) {
        val disasterAdapter = DisasterAdapter(onDisasterItemClick = { disasterItem ->
            val position = Util.getLatLngFormat(
                disasterItem.latitude, disasterItem.longitude
            )
            moveCameraAction(mMap = mMap, builder = null, location = position, zoom = 14f)
        })
        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            homeViewModel.getPeriodicDisasterData(startDate, endDate).observe(viewLifecycleOwner) { disaster ->
                    if (disaster != null) {
                        when (disaster) {
                            is Results.Loading -> {
                                showLoading(true)
                            }
                            is Results.Success -> {
                                setDisasterData(disaster.data, disasterAdapter)
                            }
                            is Results.Error -> {
                                showLoading(false)
                            }
                        }
                    }
                }
        } else {
            homeViewModel.getAllDisasterData(locFilter, disasterFilter)
                ?.observe(viewLifecycleOwner) {
                    setDisasterData(it, disasterAdapter)
                }
        }
    }

    private fun setDisasterData(
        disasterData: List<DisasterEntity>, disasterAdapter: DisasterAdapter
    ) {
        mMap.clear()
        disasterAdapter.submitList(disasterData)

        binding.bottomSheetSection.rvDisasterList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = disasterAdapter
        }

        showLoading(false, disasterData)
        if (disasterData.isNotEmpty()) {
            placeMarkerOnMap(mMap, disasterData)
        }
    }

    private fun setFilterList() {
        val filterAdapter = FilterAdapter(onDisasterFilterClick = { disasterFilter ->
            val searchLocQuery = getAreaCode(binding.svSearchLocation.query.toString())
            if (latestFilter == disasterFilter) {
                homeViewModel.saveLatestFilter("")
                if (searchLocQuery.isNotEmpty()) getDisasterData(locFilter = searchLocQuery) else getDisasterData()
            } else {
                homeViewModel.saveLatestFilter(disasterFilter)
                if (searchLocQuery.isNotEmpty()) getDisasterData(
                    locFilter = searchLocQuery, disasterFilter = disasterFilter
                )
                else getDisasterData(disasterFilter = disasterFilter)
            }
        }, onDisasterDrawable = { disasterFilter, ivFilterStatus ->
            homeViewModel.getLatestFilter().observe(this) { latestFilter ->
                this.latestFilter = latestFilter
                if (disasterFilter == latestFilter) {
                    ivFilterStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivFilterStatus.context, R.drawable.baseline_remove_24
                        )
                    )
                } else {
                    ivFilterStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivFilterStatus.context, R.drawable.baseline_add_24
                        )
                    )
                }
            }
        })
        filterAdapter.submitList(Constant.FILTER_TYPE)
        binding.rvFilter.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = filterAdapter
        }
    }

    private fun showLoading(isLoading: Boolean, data: List<DisasterEntity>? = null) {
        if (isLoading) {
            binding.bottomSheetSection.apply {
                progressBar.visibility = View.VISIBLE
                rvDisasterList.visibility = View.INVISIBLE
                tvNoData.visibility = View.GONE
            }
        } else {
            binding.bottomSheetSection.apply {
                progressBar.visibility = View.GONE
                tvNoData.visibility = View.GONE
                rvDisasterList.visibility = View.VISIBLE

                if (data?.isEmpty() == true) {
                    tvNoData.visibility = View.VISIBLE
                    rvDisasterList.visibility = View.GONE
                }
            }
        }
    }

    private fun getFloodGaugesData() {
        homeViewModel.getFloodGaugesData().observe(viewLifecycleOwner) { floodGauges ->
            if (floodGauges != null) {
                when (floodGauges) {
                    is Results.Loading -> {
                        showLoading(true)
                    }
                    is Results.Success -> {
                        showLoading(false)
                        val floodGaugesData =
                            floodGauges.data.floodGaugesResult.objects.output.geometries

                        if (floodGaugesData.isNotEmpty()) {
                            for (element in floodGaugesData) {
                                lifecycleScope.launch {
                                    setPeriodicWorkManager(requireActivity(), element)
                                    delay(5000L)
                                }
                            }
                        }
                    }
                    is Results.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}