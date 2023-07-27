package com.example.disasteralert.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disasteralert.R
import com.example.disasteralert.ViewModelFactory
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.remote.response.GeometriesItem
import com.example.disasteralert.databinding.FragmentHomeBinding
import com.example.disasteralert.helper.Constant
import com.example.disasteralert.helper.SettingPreferences
import com.example.disasteralert.helper.Util
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.ShapeAppearanceModel


class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    private lateinit var lastLocation: Location
    private lateinit var lastPinLocation: LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latestFilter: String = ""

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireActivity().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), pref)
        val viewModel: HomeViewModel by viewModels { factory }

        init(viewModel)

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

    private fun init(viewModel: HomeViewModel) {
        mapFragment =
            childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getDisasterData(viewModel)
        setFilterList(viewModel)
        setSearchLayout(viewModel)
        modifyBottomSheet()
        setBottomSheet()
    }

    private fun setBottomSheet() {

        val bottomSheet = binding.bottomSheetSection.mcvBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("Peak",slideOffset.toString())
                if (slideOffset in 0.3..0.7)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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

    private fun setSearchLayout(viewModel: HomeViewModel) {
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
                getDisasterData(viewModel, areaKey)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    binding.cvSuggestion.visibility = View.GONE
                    getDisasterData(viewModel)
                } else if (newText.length >= 3) {
                    binding.cvSuggestion.visibility = View.VISIBLE

                    listAdapter.filter.filter(newText)

                    binding.lvSuggestion.onItemClickListener =
                        AdapterView.OnItemClickListener { adapterView, view, position, id ->
                            val selectedItem = adapterView.getItemAtPosition(position) as String
                            binding.svSearchLocation.setQuery(selectedItem, false)
                            binding.cvSuggestion.visibility = View.GONE
                            val areaKey =
                                Constant.AREA.entries.find { it.value == selectedItem }?.key.toString()
                            getDisasterData(viewModel, areaKey)
                        }
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        val locationButton =
            (mapFragment.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            )
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams

        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

        rlp.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
        rlp.addRule(RelativeLayout.ALIGN_END, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp.setMargins(30, 0, 0, 75);

        googleMap.setPadding(0, 0, 0, 150)

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = Util.getLatLngFormat(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 16f))
            }
        }
    }

    private fun getDisasterData(
        viewModel: HomeViewModel, locFilter: String = "", disasterFilter: String = ""
    ) {
        val disasterAdapter = DisasterAdapter(onDisasterItemClick = { disasterItem ->
            val position = Util.getLatLngFormat(
                disasterItem.coordinates[1] as Double, disasterItem.coordinates[0] as Double
            )
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    position, 12f
                )
            )
        })
        var disasterData: List<GeometriesItem>
        viewModel.getAllDisasterData(locFilter, disasterFilter)
            .observe(viewLifecycleOwner) { disaster ->
                if (disaster != null) {
                    when (disaster) {
                        is Results.Loading -> {
                            showLoading(true)
                        }
                        is Results.Success -> {
                            showLoading(false)
                            disasterData = disaster.data.result.objects.output.geometries
                            disasterAdapter.submitList(disasterData)

                            binding.bottomSheetSection.rvDisasterList.apply {
                                layoutManager = LinearLayoutManager(context)
                                setHasFixedSize(true)
                                adapter = disasterAdapter
                                visibility = if (disasterData.isEmpty()) View.GONE else View.VISIBLE
                            }

                            if (disasterData.isEmpty()) {
                                Toast.makeText(
                                    requireActivity(), "There is no data", Toast.LENGTH_SHORT
                                ).show()
                                binding.bottomSheetSection.tvNoData.visibility = View.VISIBLE
                            } else {
                                binding.bottomSheetSection.tvNoData.visibility = View.GONE
                            }


                            mMap.clear()
                            placeMarkerOnMap(disasterData)
                            if (locFilter.isNotEmpty() || disasterFilter.isNotEmpty()) mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    lastPinLocation, 12f
                                )
                            )
                        }
                        is Results.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                requireActivity(), disaster.error, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.bottomSheetSection.apply {
                progressBar.visibility = View.VISIBLE
                rvDisasterList.visibility = View.INVISIBLE
                tvNoData.visibility = View.GONE
            }

        } else {
            binding.bottomSheetSection.apply {
                progressBar.visibility = View.GONE
                tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun placeMarkerOnMap(disasterData: List<GeometriesItem>) {
        disasterData.forEach {
            val position =
                Util.getLatLngFormat(it.coordinates[1] as Double, it.coordinates[0] as Double)
            lastPinLocation = position
            val markerOptions = MarkerOptions().position(position)
            markerOptions.title("$position")
            mMap.addMarker(markerOptions)
        }
    }

    private fun setFilterList(viewModel: HomeViewModel) {
        val filterAdapter = FilterAdapter(onDisasterFilterClick = { disasterFilter ->
            if (latestFilter == disasterFilter) {
                viewModel.saveLatestFilter("")
                getDisasterData(viewModel)
            } else {
                viewModel.saveLatestFilter(disasterFilter)
                getDisasterData(viewModel, disasterFilter = disasterFilter)
            }
        }, onDisasterDrawable = { disasterFilter, ivFilterStatus ->
            viewModel.getLatestFilter().observe(this) { latestFilter ->
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

    override fun onMarkerClick(p0: Marker): Boolean = false
}