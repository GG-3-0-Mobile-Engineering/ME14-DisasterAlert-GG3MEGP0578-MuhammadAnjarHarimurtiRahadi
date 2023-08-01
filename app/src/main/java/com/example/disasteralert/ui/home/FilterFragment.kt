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

        binding.apply {
            btnStartDate.setOnClickListener(this@FilterFragment)
            btnEndDate.setOnClickListener(this@FilterFragment)
            btnRemove.setOnClickListener(this@FilterFragment)
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
        fun onFilterChosen(startDate: String, endDate: String)
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
            R.id.btn_remove -> {
                filterDialogListener?.onFilterChosen("", "")
                dialog?.dismiss()
            }
            R.id.btn_apply -> {
                val startDate = binding.tvStartDate.text.toString()
                val endDate = binding.tvEndDate.text.toString()

                filterDialogListener?.onFilterChosen(startDate, endDate)
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
}