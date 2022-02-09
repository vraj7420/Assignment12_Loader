package com.example.loader.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loader.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var alertContactReadPermission: AlertDialog
    private lateinit var alertFileAccessPermission: AlertDialog
    private var btnContactClick = false
    private var btnMusicClick = false
    private var buttonClickId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setListener()
    }

    private fun init() {
        alertContactReadPermission = AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_dialog_title))
            .setPositiveButton(
                getString(R.string.permission_dialog_positive_button),
                ({ _: DialogInterface, _: Int ->
                    navigateToSettingScreen()
                })
            ).setNegativeButton(
                getString(R.string.permission_dialog_negative_button),
                ({ dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })
            ).setCancelable(false)
            .setMessage(getString(R.string.permission_dialog_message_for_contact_read))
            .create()
        alertFileAccessPermission = AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_dialog_title))
            .setPositiveButton(
                getString(R.string.permission_dialog_positive_button),
                ({ _: DialogInterface, _: Int ->
                    navigateToSettingScreen()
                })
            ).setNegativeButton(
                getString(R.string.permission_dialog_negative_button),
                ({ dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })
            ).setCancelable(false)
            .setMessage(getString(R.string.permission_dialog_message_for_music))
            .create()

    }

    private fun setListener() {
        btnContact.setOnClickListener {
            btnContactClick = true
            checkContactReadPermission()
        }
        btnMusic.setOnClickListener {
            btnMusicClick = true
            checkFileAccessPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        when (buttonClickId) {
             0->return
             1->{
                 checkContactReadPermission()
                 alertContactReadPermission.dismiss()
                 btnContactClick=false
                 buttonClickId=0
             }
             2->{
                 checkFileAccessPermission()
                 alertFileAccessPermission.dismiss()
                 btnMusicClick=false
                 buttonClickId=0
             }
        }
    }

    private fun checkFileAccessPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    alertFileAccessPermission.dismiss()
                    val intent = Intent(this@MainActivity, MusicActivity::class.java)
                    startActivity(intent)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (p0?.isPermanentlyDenied == true) {
                        alertFileAccessPermission.show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
    }

    private fun checkContactReadPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    alertContactReadPermission.dismiss()
                    val intent = Intent(this@MainActivity, ContactActivity::class.java)
                    startActivity(intent)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (p0?.isPermanentlyDenied == true) {
                        alertContactReadPermission.show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
    }

    private fun navigateToSettingScreen() {
        if (btnContactClick) {
            buttonClickId = 1
        }
        if (btnMusicClick) {
            buttonClickId = 2
        }
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}