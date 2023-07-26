package com.example.disasteralert.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.example.disasteralert.R
import com.example.disasteralert.ViewModelFactory
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.remote.response.GeometriesItem
import com.example.disasteralert.databinding.FragmentHomeBinding
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
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels { factory }

        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getDisasterData(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
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

    private fun getDisasterData(viewModel: HomeViewModel) {
        viewModel.getAllDisasterData().observe(viewLifecycleOwner) { disaster ->
            if (disaster != null) {
                when (disaster) {
                    is Results.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Results.Success -> {
//                        binding.progressBar.visibility = View.GONE
                        val disasterData = disaster.data.result
                        placeMarkerOnMap(disasterData.objects.output.geometries)
                    }
                    is Results.Error -> {
//                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireActivity(),
                            "Terjadi kesalahan" + disaster.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun placeMarkerOnMap(disasterData: List<GeometriesItem>) {
        disasterData.forEach {
            val position = LatLng(it.coordinates[1] as Double, it.coordinates[0] as Double)
            val markerOptions = MarkerOptions().position(position)
            markerOptions.title("$position")
            mMap.addMarker(markerOptions)
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean = false
}