package com.practical.products.ui.product.dialog

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.practical.products.R
import com.practical.products.databinding.DialogSelectPhotoMethodBinding

/**
 * Gallery OR Camera Image selection BottomSheetDialogFragment
 */
class SelectPhotoMethodDialogFragment(private val onConfirmMethodCallback: (Int) -> Unit) :
    BaseBottomSheetFragment<DialogSelectPhotoMethodBinding>(R.layout.dialog_select_photo_method) {

    override val collapsedHeightRatio: Double = 0.3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialogStyle)

    private fun initialListener() = with(binding) {
        menuTakePhotoContainer.setOnClickListener {
            onConfirmMethodCallback.invoke(CONFIRM_TAKE_PHOTO)
            dismiss()
        }

        menuSelectPhotoContainer.setOnClickListener {
            onConfirmMethodCallback.invoke(CONFIRM_GALLERY_PHOTO)
            dismiss()
        }
    }

    companion object {
        const val TAG_SELECT_PHOTO = "TAG_SELECT_PHOTO"
        const val CONFIRM_TAKE_PHOTO = 1
        const val CONFIRM_GALLERY_PHOTO = 2
        fun newInstance(onConfirmMethodCallback: (Int) -> Unit) =
            SelectPhotoMethodDialogFragment(onConfirmMethodCallback)
    }
}
