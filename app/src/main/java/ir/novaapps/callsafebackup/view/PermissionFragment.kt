package ir.novaapps.callsafebackup.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.R
import ir.novaapps.callsafebackup.databinding.IntroFragmentBinding
import ir.novaapps.callsafebackup.databinding.PermissionFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PermissionFragment : BaseFragment<PermissionFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> PermissionFragmentBinding
        get() = PermissionFragmentBinding::inflate


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnGetAllPermission.setOnClickListener{
                checkContactPermission()
            }
        }
    }






    private val openSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        checkContactPermission() // بعد از بازگشت از تنظیمات دوباره مجوزها بررسی می‌شود
    }

    private fun checkContactPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                checkCallLogPermission() // اگر مجوز مخاطبین داده شده باشد، به سراغ بعدی می‌رود
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_CONTACTS
            ) -> {
                showPermissionDialog(
                    title = "مجوز مخاطبین",
                    message = "برای گرفتن بکاپ باید دسترسی مخاطبین را بدهید.",
                    onClickOk = {
                        requestPermission(Manifest.permission.READ_CONTACTS, 1001)
                    }
                )
            }

            else -> {
                showPermissionDialog(
                    title = "رفتن به تنظیمات",
                    message = "برای گرفتن بکاپ باید دسترسی مخاطبین را بدهید. به تنظیمات رفته و مجوز را فعال کنید.",
                    onClickOk = { openSettings() }
                )
            }
        }
    }

    private fun checkCallLogPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED -> {
                // اجرای اصلی کدها پس از دریافت تمام مجوزها
                performBackup()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_CALL_LOG
            ) -> {
                showPermissionDialog(
                    title = "مجوز گزارش تماس",
                    message = "برای گرفتن بکاپ باید دسترسی گزارش تماس را بدهید.",
                    onClickOk = {
                        requestPermission(Manifest.permission.READ_CALL_LOG, 1002)
                    }
                )
            }

            else -> {
                showPermissionDialog(
                    title = "رفتن به تنظیمات",
                    message = "برای گرفتن بکاپ باید دسترسی گزارش تماس را بدهید. به تنظیمات رفته و مجوز را فعال کنید.",
                    onClickOk = { openSettings() }
                )
            }
        }
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }

    private fun openSettings() {
        openSettingsLauncher.launch(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireContext().packageName, null)
            )
        )
    }

    private fun showPermissionDialog(title: String, message: String, onClickOk: () -> Unit) {
        dialogPermission(title, message, "تایید", onClickOk)
    }

    private fun performBackup() {
        // اجرای کد اصلی برای گرفتن بکاپ
        findNavController().navigate(PermissionFragmentDirections.actionPermissionFragmentToHomeFragment())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkCallLogPermission()
                } else {
                    showPermissionDialog(
                        title = "رفتن به تنظیمات",
                        message = "برای گرفتن بکاپ باید دسترسی مخاطبین را بدهید. به تنظیمات رفته و مجوز را فعال کنید.",
                        onClickOk = { openSettings() }
                    )
                }
            }

            1002 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    performBackup()
                } else {
                    showPermissionDialog(
                        title = "رفتن به تنظیمات",
                        message = "برای گرفتن بکاپ باید دسترسی گزارش تماس را بدهید. به تنظیمات رفته و مجوز را فعال کنید.",
                        onClickOk = { openSettings() }
                    )
                }
            }
        }
    }

    private fun dialogPermission(
        title:String
        ,description:String,
        textPositiveButton:String,
        onClickOk : ()->Unit){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(description)
            .setCancelable(false)
            .setPositiveButton(textPositiveButton,
                DialogInterface.OnClickListener { dialogInterface, i ->onClickOk()  })
            .setNeutralButton("صرفنظر", DialogInterface.OnClickListener { dialogInterface, i ->  })
            .create()
            .show()
    }
}