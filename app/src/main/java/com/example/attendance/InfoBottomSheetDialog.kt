package com.example.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.attendance.databinding.ItemBottomsheetBinding
import com.example.attendance.model.Student
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheetDialog(private val student: Student) : BottomSheetDialogFragment() {
    private lateinit var binding: ItemBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = ItemBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.idBtnDismiss.setOnClickListener {
            //dialog?.dismiss()
            dismiss()
        }*/
        binding.student = student
       /* val bottomSheet: FrameLayout =
            dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
        // Height of the view
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        // Behavior of the bottom sheet
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.apply {
            peekHeight = resources.displayMetrics.heightPixels // Pop-up height
            state = BottomSheetBehavior.STATE_EXPANDED // Expanded state
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }*/
    }

    /* override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         // used to show the bottom sheet dialog
         dialog?.setOnShowListener { it ->
             val d = it as BottomSheetDialog
             val bottomSheet =
                 d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
             bottomSheet?.let {
                 val behavior = BottomSheetBehavior.from(it)
                 behavior.state = BottomSheetBehavior.STATE_EXPANDED
             }
         }
         return super.onCreateDialog(savedInstanceState)
     }*/
    companion object {
        const val TAG = "InfoBottomSheetDialog"
    }
}