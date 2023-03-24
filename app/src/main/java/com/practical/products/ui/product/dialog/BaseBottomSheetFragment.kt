package com.practical.products.ui.product.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Base class for BottomSheetDialogFragment
 */
open class BaseBottomSheetFragment<DB : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    BottomSheetDialogFragment() {

    protected lateinit var binding: DB
    private lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>
    protected open val collapsedHeightRatio = 0.7

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setOnShowListener { dialog ->
                val d = dialog as BottomSheetDialog
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    ?.let { bottomSheet ->
                        val params = bottomSheet.layoutParams
                        val windowMetrics = WindowMetricsCalculator.getOrCreate()
                            .computeCurrentWindowMetrics(requireActivity())
                        val currentBounds = windowMetrics.bounds
                        val windowHeight = currentBounds.height()
                        params.height = windowHeight
                        bottomSheet.layoutParams = params
                        sheetBehavior = BottomSheetBehavior.from(bottomSheet).apply {
                            isHideable = true
                            setPeekHeight((windowHeight * collapsedHeightRatio).toInt(), true)
                            setState(STATE_COLLAPSED)
                        }
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, true)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
