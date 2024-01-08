package com.dn.vdp.base_mvvm.presentation.fragments.task

import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dn.vdp.base_module.presentation.BaseFragment
import com.dn.vdp.base_module.utils.clicks
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.AuthNavigationDirections
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.FragmentTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : BaseFragment<TaskViewModel, FragmentTaskBinding>(R.layout.fragment_task) {
    override val binding by viewBindings(FragmentTaskBinding::bind)
    override val viewModel by viewModels<TaskViewModel>()

    override fun setupViews() {
//        binding.loginRegisterBtn.clicks {
//            findNavController().navigate(AuthNavigationDirections.actionAuthToMain())
//        }



        binding.btnTop.setOnClickListener {
            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(requireContext())

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            // on below line we are creating a variable for our button
            // which we are using to dismiss our dialog.
            val btnClose = view.findViewById<ImageView>(R.id.img_close)
            val ll_color = view.findViewById<LinearLayout>(R.id.ll_color)

            val list = [R.color.black,R.color.black,R.color.black,R.color.black]

            val layout = LinearLayoutManager(requireContext())
         //   layout.setMeasuredDimension(50,50)

            for (i in 0 until 5){
                val color = TextView(requireContext())
                color.width = 50
                color.height = 50

                color.setBackgroundColor(resources.getColor(R.color.black))
                color.setPadding(5,5,5,5)
                ll_color.addView(color)
            }

            // on below line we are adding on click listener
            // for our dismissing the dialog button.
            btnClose.setOnClickListener {
                // on below line we are calling a dismiss
                // method to close our dialog.
                dialog.dismiss()
            }
            // below line is use to set cancelable to avoid
            // closing of dialog box when clicking on the screen.
            dialog.setCancelable(false)

            // on below line we are setting
            // content view to our view.
            dialog.setContentView(view)

            // on below line we are calling
            // a show method to display a dialog.
            dialog.show()
        }
    }

    override fun bindViewModel() {
    }
}