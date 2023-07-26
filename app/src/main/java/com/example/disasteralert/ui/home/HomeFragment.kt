package com.example.disasteralert.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disasteralert.R
import com.example.disasteralert.ViewModelFactory
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.remote.response.GeometriesItem
import com.example.disasteralert.databinding.FragmentHomeBinding
import com.example.disasteralert.helper.Constant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    private lateinit var lastLocation: Location
    private lateinit var lastPinLocation: LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels { factory }

        mapFragment =
            childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getDisasterData(viewModel)

        setFilterList(viewModel)

        setSearchLayout(viewModel)
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
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) binding.cvSuggestion.visibility = View.GONE
                else binding.cvSuggestion.visibility = View.VISIBLE

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
        rlp.setMargins(30, 0, 0, 40);

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
                val currentLatLong = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 16f))
            }
        }
    }

    private fun getDisasterData(
        viewModel: HomeViewModel, locFilter: String = "", disasterFilter: String = ""
    ) {
        viewModel.getAllDisasterData(locFilter, disasterFilter)
            .observe(viewLifecycleOwner) { disaster ->
                if (disaster != null) {
                    when (disaster) {
                        is Results.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
                        }
                        is Results.Success -> {
//                        binding.progressBar.visibility = View.GONE
                            val disasterData = disaster.data.result
                            mMap.clear()
                            placeMarkerOnMap(disasterData.objects.output.geometries)
                            if (locFilter.isNotEmpty() || disasterFilter.isNotEmpty()) mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    lastPinLocation, 12f
                                )
                            )
                        }
                        is Results.Error -> {
//                        binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireActivity(), disaster.error, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }

    private fun placeMarkerOnMap(disasterData: List<GeometriesItem>) {
        disasterData.forEach {
            val position = LatLng(it.coordinates[1] as Double, it.coordinates[0] as Double)
            lastPinLocation = position
            val markerOptions = MarkerOptions().position(position)
            markerOptions.title("$position")
            mMap.addMarker(markerOptions)
        }
    }

    private fun setFilterList(viewModel: HomeViewModel) {
        val adapter =
            FilterAdapter(Constant.FILTER_TYPE, onDisasterFilterClick = { disasterFilter ->
                getDisasterData(viewModel, disasterFilter = disasterFilter)
            })
        binding.rvFilter.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFilter.adapter = adapter
    }

    override fun onMarkerClick(p0: Marker): Boolean = false
}