package com.example.base.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.example.base.R
import com.example.base.extensions.dp
import com.example.base.extensions.topRoundedCorner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.parcelize.Parcelize

class ActionSheet : BottomSheetDialogFragment(), View.OnClickListener {

    var afterViewInflatedAction: ((view: View) -> Unit)? = null

    var clickListener: View.OnClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = layoutInflater.inflate(
            R.layout.action_sheet_base,
            container, false
        ) as LinearLayout
        requireArguments().getParcelableArray(ARG_ITEMS)?.forEach { item ->
            when (item) {
                is TextCell -> {
                    val v = TextView(requireContext())
                    v.id = item.id
                    v.text = item.text
                    v.textSize = item.textSize
                    v.setTextColor(item.textColor)
                    item.bindPadding(v)
                    view.addView(
                        v,
                        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    )
                }
                is ButtonCell -> {
                    val v = Button(requireContext())
                    v.id = item.id
                    v.text = item.text
                    v.textSize = item.textSize
                    v.setTextColor(item.textColor)
                    v.setOnClickListener(this)
                    v.background = null
                    v.gravity = Gravity.CENTER_VERTICAL or Gravity.START
                    item.bindPadding(v)
                    view.addView(
                        v,
                        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    )
                }
                is CustomContent -> {
                    val v = layoutInflater.inflate(item.layoutResId, view, false)
                    view.addView(
                        v,
                        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    )
                }
            }
        }
        afterViewInflatedAction?.invoke(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.topRoundedCorner(16.dp(requireContext()))
    }

    override fun onClick(v: View?) {
        v?.let {
            clickListener?.onClick(v)
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        afterViewInflatedAction = null
        clickListener = null
    }

    companion object {
        private const val ARG_ITEMS = "arg_items"

        fun withItems(
            items: Array<CellItem>,
            afterViewInflatedAction: ((view: View) -> Unit)? = null,
            clickListener: View.OnClickListener? = null,
        ): ActionSheet {
            val sheet = ActionSheet()
            sheet.clickListener = clickListener
            sheet.afterViewInflatedAction = afterViewInflatedAction
            sheet.arguments = Bundle().apply {
                putParcelableArray(ARG_ITEMS, items)
            }
            return sheet
        }

        fun ActionSheet.show(fm: FragmentManager) {
            show(fm, "ActionSheet")
        }
    }

    sealed interface CellItem : Parcelable

    interface PaddingCell : CellItem {
        val topPaddingDp: Int
        val bottomPaddingDp: Int
        val startPaddingDp: Int
        val endPaddingDp: Int

        fun bindPadding(view: View) {
            view.setPadding(
                startPaddingDp.dp(view.context).toInt(),
                topPaddingDp.dp(view.context).toInt(),
                endPaddingDp.dp(view.context).toInt(),
                bottomPaddingDp.dp(view.context).toInt()
            )
        }
    }

    @Parcelize
    data class TextCell(
        val id: Int,
        val text: String,
        val textSize: Float = 14f,
        @ColorInt val textColor: Int = Color.BLACK,
        override val topPaddingDp: Int = 10,
        override val bottomPaddingDp: Int = 10,
        override val startPaddingDp: Int = 16,
        override val endPaddingDp: Int = 16,
    ) : PaddingCell

    @Parcelize
    data class ButtonCell(
        val id: Int,
        val text: String,
        val textSize: Float = 14f,
        @ColorInt val textColor: Int = Color.BLACK,
        override val topPaddingDp: Int = 10,
        override val bottomPaddingDp: Int = 10,
        override val startPaddingDp: Int = 16,
        override val endPaddingDp: Int = 16,
    ) : PaddingCell

    @Parcelize
    data class CustomContent(@LayoutRes val layoutResId: Int) : CellItem

}