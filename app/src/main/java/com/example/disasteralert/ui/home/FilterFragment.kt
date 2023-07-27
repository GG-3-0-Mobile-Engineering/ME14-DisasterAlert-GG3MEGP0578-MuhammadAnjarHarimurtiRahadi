package com.example.disasteralert.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import com.example.disasteralert.R
import com.example.disasteralert.databinding.FragmentFilterBinding
import com.example.disasteralert.helper.Constant
import com.example.disasteralert.helper.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*


class FilterFragment : DialogFragment(), View.OnClickListener, DatePickerFragment.DialogDateListener {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private var filterDialogListener: OnFilterDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearchLayout()

        binding.apply {
            btnStartDate.setOnClickListener(this@FilterFragment)
            btnEndDate.setOnClickListener(this@FilterFragment)
            btnApply.setOnClickListener(this@FilterFragment)
            btnCancel.setOnClickListener(this@FilterFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = parentFragment
        if (fragment is HomeFragment) {
            this.filterDialogListener = fragment.filterDialogListener
        }
    }
    override fun onDetach() {
        super.onDetach()
        this.filterDialogListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface OnFilterDialogListener {
        fun onFilterChosen(startDate: String, endDate: String, province: String)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_start_date -> {
                val datePickerFragment = DatePickerFragment(Constant.ARG_DATE_START)
                datePickerFragment.show(childFragmentManager, Constant.DATE_PICKER_TAG)
            }
            R.id.btn_end_date -> {
                val datePickerFragment = DatePickerFragment(Constant.ARG_DATE_END)
                datePickerFragment.show(childFragmentManager, Constant.DATE_PICKER_TAG)
            }
            R.id.btn_apply -> {
                val startDate = binding.tvStartDate.text.toString()
                val endDate = binding.tvEndDate.text.toString()
                val province = binding.svSearchLocation.query.toString()
                val areaKey =
                    Constant.AREA.entries.find { it.value == province }?.key ?: ""

                filterDialogListener?.onFilterChosen(startDate, endDate, areaKey)
                dialog?.dismiss()
            }
            R.id.btn_cancel -> dialog?.cancel()
        }
    }

    override fun onDialogDateSet(
        tag: String?,
        year: Int,
        month: Int,
        dayOfMonth: Int,
        dateStatus: String
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        if (dateStatus == Constant.ARG_DATE_START)
            binding.tvStartDate.text = dateFormat.format(calendar.time)
        else if (dateStatus == Constant.ARG_DATE_END)
            binding.tvEndDate.text = dateFormat.format(calendar.time)
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
                binding.lvSuggestion.visibility = View.GONE
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    binding.lvSuggestion.visibility = View.GONE
                } else if (newText.length >= 3) {
                    binding.lvSuggestion.visibility = View.VISIBLE

                    listAdapter.filter.filter(newText)

                    binding.lvSuggestion.onItemClickListener =
                        AdapterView.OnItemClickListener { adapterView, view, position, id ->
                            val selectedItem = adapterView.getItemAtPosition(position) as String
                            binding.svSearchLocation.setQuery(selectedItem, false)
                            binding.lvSuggestion.visibility = View.GONE
                        }
                }
                return false
            }
        })
    }
}