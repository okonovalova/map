package com.example.myapplication.ui

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentNewPointDialogBinding
import kotlin.random.Random

class NewPointDialogFragment : BottomSheetDialogFragment() {

   lateinit var onAddPointListener: (String) -> Unit

    companion object {
        const val TAG = "NewPointDialogFragmentTAG"
        fun newInstance(
            onAddPointListener: (String) -> Unit
        ): NewPointDialogFragment {
            return  NewPointDialogFragment().apply {
                this.onAddPointListener = onAddPointListener
                arguments = Bundle().apply {
                }
            }
    }}

    private var _binding: FragmentNewPointDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPointDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding?.imgClose?.setOnClickListener {
            dismissAllowingStateLoss()
        }
        _binding?.btnCancel?.setOnClickListener {
            dismissAllowingStateLoss()
        }
        _binding?.btnUpdate?.setOnClickListener {
            val name = _binding?.editTextPointName?.text?.toString().orEmpty()
            if (name.isEmpty()) {
                val random = Random.nextInt(100000)
                onAddPointListener.invoke("New Point $random")
            } else {
                onAddPointListener.invoke(name)
            }
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}