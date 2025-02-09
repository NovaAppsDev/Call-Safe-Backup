package ir.novaapps.ui

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView

class DialogProgress {
    companion object {

        fun resultDialog(dialog: Dialog?) {
            dialog?.setContentView(R.layout.design_dialog_2)

            // تنظیم ظاهر دیالوگ
            dialog?.window?.setBackgroundDrawable(ColorDrawable(0))

            val animationResultTrue = dialog?.findViewById<LottieAnimationView>(R.id.animResultTrue)
            val txtTitle = dialog?.findViewById<TextView>(R.id.txtIsLoading)

            txtTitle?.text = "انجام شد."
            dialog?.setCancelable(false)
            // نمایش دیالوگ
            dialog?.show()
        }

    }
}