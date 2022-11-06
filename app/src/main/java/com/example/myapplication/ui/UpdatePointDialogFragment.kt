package com.example.myapplication.ui

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentUpdatePointDialogBinding
import kotlin.random.Random

class UpdatePointDialogFragment : BottomSheetDialogFragment() {

    lateinit var onDeletePointListener: () -> Unit
    lateinit var onRenamePointListener: (String) -> Unit

    companion object {
        const val TAG = "UpdatePointDialogFragment"
        private const val ARG_TITLE = "ARG_TITLE"
        fun newInstance(
            onDeletePointListener: () -> Unit,
            onRenamePointListener: (String) -> Unit,
            title: String
        ): UpdatePointDialogFragment {
            return UpdatePointDialogFragment().apply {
                this.onRenamePointListener = onRenamePointListener
                this.onDeletePointListener = onDeletePointListener
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
        }
    }

    private var _binding: FragmentUpdatePointDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdatePointDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding?.imgClose?.setOnClickListener {
            dismissAllowingStateLoss()
        }
        _binding?.editTextPointName?.setText(arguments?.getString(ARG_TITLE).orEmpty())
        _binding?.btnRemove?.setOnClickListener {
            onDeletePointListener.invoke()
            dismissAllowingStateLoss()
        }
        _binding?.btnCreate?.setOnClickListener {
            val name = _binding?.editTextPointName?.text?.toString().orEmpty()
            if (name.isEmpty()) {
                val random = Random.nextInt(10000)
                onRenamePointListener.invoke("New Point $random")
            } else {
                onRenamePointListener.invoke(name)
            }
            dismissAllowingStateLoss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}