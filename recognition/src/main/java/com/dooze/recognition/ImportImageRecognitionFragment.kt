package com.dooze.recognition

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.dooze.recognition.databinding.FragmentImportImageRecognitionBinding
import com.dooze.recognition.model.toStringText
import com.dooze.recognition.window.FloatWindow
import com.dooze.recognition.window.ProjectionScreen
import com.dooze.recognition.window.ProjectionService
import com.example.base.extensions.*
import com.example.base.ui.ActionSheet
import com.example.base.ui.ActionSheet.Companion.show
import com.example.base.ui.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.flow.*

class ImportImageRecognitionFragment :
    BaseFragment<ImportImageRecognitionViewModel>(
        R.layout.fragment_import_image_recognition,
        ImportImageRecognitionViewModel::class.java,
        activityModel = true
    ), View.OnClickListener, View.OnLongClickListener, Toolbar.OnMenuItemClickListener {


    private val binding by viewBinding(FragmentImportImageRecognitionBinding::bind)

    private var curResultSheet: DialogFragment? = null
    private var projectionScreen: ProjectionScreen? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnImport.setOnClickListener(this)
        binding.imgPreview.setOnLongClickListener(this)
        binding.toolbar.inflateMenu(R.menu.recognition_menu)
        binding.toolbar.setOnMenuItemClickListener(this)
        viewModel.recognitionState.observe(viewLifecycleOwner) {
            when (it) {
                Loading -> {
                    binding.loadingBar.show()
                }
                else -> {
                    binding.loadingBar.hide()
                }
            }
        }
        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            binding.imgPreview.setImageURI(uri)
        }
        launch {
            viewModel.recognitionResult.collect { text ->
                text ?: return@collect
                if (text.text.isEmpty()) {
                    view.showSnack(R.string.no_recognition_result_tips)
                    return@collect
                }
                curResultSheet?.dismiss()
                val sheet = ResultSheet(text)
                sheet.show(parentFragmentManager, "ResultSheet")
                curResultSheet = sheet
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnImport -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, REQ_CODE_FROM_PICK_IMG)
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.imgPreview -> {
                viewModel.reRecognition(requireContext())
            }
        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.rec_accessibility -> {
                if (PermissionUtils.isOverlayEnable(requireContext())) {
                    FloatWindow(
                        requireContext(),
                        requireContext().applicationContext as LifecycleOwner
                    ) { isStart: Boolean ->
                        if (isStart) {
                            (projectionScreen ?: ProjectionScreen().also {
                                projectionScreen = it
                            }).init(this)
                        }
                    }
                } else {
                    ActionSheet.withItems(
                        arrayOf(
                            ActionSheet.TextCell(
                                com.example.base.R.id.common_title,
                                getString(R.string.recognition_overlay_permission_title)
                            ),
                            ActionSheet.ButtonCell(
                                com.example.base.R.id.common_ok,
                                getString(R.string.common_goto_setting),
                                textColor = getColorCompat(com.example.base.R.color.colorOnPrimary)
                            )
                        )
                    ) {
                        when (it.id) {
                            com.example.base.R.id.common_ok -> {
                                PermissionUtils.gotoOverlaySetting(requireActivity())
                            }
                        }
                    }.show(parentFragmentManager)
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_FROM_PICK_IMG && data != null) {
                data.data?.let { imgUri ->
                    viewModel.recognitionImage(requireContext(), imgUri)
                }
            } else if (ProjectionScreen.isProjectionReq(requestCode)) {
                ProjectionService.start(requireContext(), resultCode, requireNotNull(data))
            }
        }
    }

    companion object {
        const val REQ_CODE_FROM_PICK_IMG = 101
    }


    class ResultSheet(private val text: Text) : BottomSheetDialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return layoutInflater.inflate(R.layout.sheet_text_result, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            view.topRoundedCorner(16.dp(requireContext()))
            val resultText = text.toStringText()
            val etText = view.findViewById<EditText>(R.id.etResult)
            etText.setText(resultText)
            val btnCopy = view.findViewById<Button>(R.id.btnCopyAll)
            btnCopy.setOnClickListener {
                val manager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                manager.setPrimaryClip(
                    ClipData.newPlainText(
                        "TextRecognition",
                        etText.text.toString()
                    )
                )
                dismiss()
            }

        }
    }

}