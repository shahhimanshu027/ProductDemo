package com.practical.products.ui.product.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practical.products.BuildConfig
import com.practical.products.R
import com.practical.products.data.response.ProductData
import com.practical.products.databinding.FragmentAddProductBinding
import com.practical.products.ui.product.adapter.DeleteSelectedImage
import com.practical.products.ui.product.adapter.ImageSelectAdapter
import com.practical.products.ui.product.dialog.SelectPhotoMethodDialogFragment
import com.practical.products.ui.product.dialog.SelectPhotoMethodDialogFragment.Companion.CONFIRM_GALLERY_PHOTO
import com.practical.products.ui.product.dialog.SelectPhotoMethodDialogFragment.Companion.CONFIRM_TAKE_PHOTO
import com.practical.products.ui.product.dialog.SelectPhotoMethodDialogFragment.Companion.TAG_SELECT_PHOTO
import com.practical.products.ui.product.viewmodel.ProductViewModel
import com.practical.products.utils.showToast
import kotlinx.coroutines.launch
import java.io.File

/**
 * Add Product screen
 */
class AddProductFragment : Fragment() {

    private var adapter: ImageSelectAdapter? = null
    private val productViewModel: ProductViewModel by activityViewModels()
    private lateinit var mBinding: FragmentAddProductBinding
    private var latestUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentAddProductBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        backPress()
        initClick()
        initData()
    }

    private fun initData() {
        mBinding.apply {
            productViewModel.productImages.clear()
            adapter = ImageSelectAdapter(
                deleteSelectedImage = object : DeleteSelectedImage {
                    override fun onDelete(uri: String) {
                        deleteImageConfirmation(uri)
                    }
                },
            )
            rvImages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvImages.adapter = adapter

            edtTitle.addTextChangedListener(
                afterTextChanged = {
                    isAddProductButtonEnabled()
                },

            )
            edtTitle.addTextChangedListener(
                afterTextChanged = {
                    isAddProductButtonEnabled()
                },
            )
            edtDescription.addTextChangedListener(
                afterTextChanged = {
                    isAddProductButtonEnabled()
                },
            )
            edtPrice.addTextChangedListener(
                afterTextChanged = {
                    isAddProductButtonEnabled()
                },
            )
            edtDiscount.addTextChangedListener(
                afterTextChanged = {
                    isAddProductButtonEnabled()
                },
            )
        }
    }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                addImage(it)
            }
        }

    private fun initClick() {
        mBinding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivAddImage.setOnClickListener {
                val isListFull = !adapter?.currentList.isNullOrEmpty() &&
                    adapter?.currentList?.size!! > 4
                if (!isListFull) {
                    SelectPhotoMethodDialogFragment.newInstance { method ->
                        when (method) {
                            CONFIRM_TAKE_PHOTO -> {
                                when {
                                    isPermissionsGranted() -> openCamera()
                                    shouldShowRationale() -> showPermissionDeniedDialog()
                                    else -> launchPermission()
                                }
                            }
                            CONFIRM_GALLERY_PHOTO -> {
                                selectImageFromGalleryResult.launch("image/*")
                            }
                        }
                    }.also {
                        it.show(childFragmentManager, TAG_SELECT_PHOTO)
                    }
                } else {
                    showToast(
                        getString(R.string.maximum_five_image),
                        requireContext(),
                    )
                }
            }
            btnAddProduct.setOnClickListener {
                if (isAddProductButtonEnabled()) {
                    productViewModel.addProductList.add(
                        ProductData(
                            id = 0,
                            title = edtTitle.text.toString(),
                            description = edtDescription.text.toString(),
                            price = edtPrice.text.toString().toInt(),
                            discountPercentage = edtDiscount.text.toString().toFloat(),
                            rating = 0.0f,
                            stock = 0,
                            brand = "",
                            category = "",
                            thumbnail = productViewModel.productImages[0],
                            images = productViewModel.productImages.toMutableList(),
                        ),
                    )
                    showToast(
                        getString(R.string.product_successfully_added),
                        requireContext(),
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun backPress() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> openCamera()
                shouldShowRationale() -> showPermissionDeniedDialog()
                else -> showMandatoryPermissionsNeedDialog()
            }
        }

    /**
     * Ask runtime permission for CAMERA
     */
    private fun launchPermission() = permissionResultLauncher.launch(Manifest.permission.CAMERA)

    /**
     * show this custom dialog to alert user denied camera permission
     */
    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setMessage(getString(R.string.permission_camera_access_required))
            setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                launchPermission()
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
        }.show()
    }

    /**
     * show this custom dialog to alert user that please go to settings to enable camera permission
     */
    private fun showMandatoryPermissionsNeedDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setMessage(getString(R.string.mandatory_permission_access_required))
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
        }.show()
    }

    private fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA,
    ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRationale() =
        shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

    private val takeImageResult = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
    ) { isSuccess ->
        if (isSuccess) {
            latestUri?.let { uri ->
                addImage(uri)
            }
        }
    }

    /**
     * launch Camera for activity result
     */
    private fun openCamera() {
        lifecycleScope.launch {
            getTmpFileUri().let { uri ->
                latestUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    /**
     * create temporary file and return file Uri
     */
    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("image_file", ".png", activity?.cacheDir)
            .apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile,
        )
    }

    /**
     * Set Image Uri in Image List and update recyclerview Adapter
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun addImage(uri: Uri) {
        productViewModel.productImages.add(uri.toString())
        adapter?.submitList(productViewModel.productImages)
        adapter?.notifyDataSetChanged()
        isAddProductButtonEnabled()
    }

    /**
     * Delete image from product images list and refresh Adapter
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteImage(uri: String) {
        productViewModel.productImages.remove(uri)
        adapter?.submitList(productViewModel.productImages)
        adapter?.notifyDataSetChanged()
    }

    /**
     * Validation for Add product Button
     * if all info available then button will be enable
     */
    private fun isAddProductButtonEnabled(): Boolean {
        mBinding.apply {
            val isAllDataSet = productViewModel.productImages.isNotEmpty() &&
                edtTitle.text.toString().isNotEmpty() &&
                edtDescription.text.toString().isNotEmpty() &&
                edtDiscount.text.toString().isNotEmpty() &&
                edtPrice.text.toString().isNotEmpty()
            btnAddProduct.isEnabled = isAllDataSet
            return isAllDataSet
        }
    }

    private fun deleteImageConfirmation(uri: String) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setMessage(getString(R.string.are_you_sure_you_want))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteImage(uri)
                isAddProductButtonEnabled()
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
        }.show()
    }
}
