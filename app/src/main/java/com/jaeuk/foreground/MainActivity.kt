package com.jaeuk.foreground

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver

/*
전화 앱과 상호 작용하기: https://developer.android.com/guide/components/intents-common#Phone
전화 앱을 대체하는 기능 제공하기: https://developer.android.com/guide/topics/ui/direct-dial
전화 수신 시 실행되는 앱 만들기: https://developer.android.com/guide/components/services#Foreground

전화 팝업을 가리거나, 화면 위에 앱을 표시하는 기능에 대해서는 아래의 링크에서 확인할 수 있다.
WindowManager: https://developer.android.com/reference/android/view/WindowManager
TYPE_SYSTEM_ALERT 참조: https://developer.android.com/reference/android/view/WindowManager.LayoutParams#TYPE_SYSTEM_ALERT
 */

// 권한 요청 및 startForegroundService() 메서드를 통해 서비스 시작
class MainActivity : AppCompatActivity(), LifecycleObserver {

    /*
        앱이 실행될 때, 권한을 체크하고 권한이 허용되면 포어그라운드 서비스를 시작하고,
        포어그라운드 서비스 시작 알림(notification)을 띄운다.
     */

    private val PERMISSIONS_REQUEST_CODE = 1
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_PHONE_NUMBERS,
        Manifest.permission.FOREGROUND_SERVICE, // api level 28 이상이어야 함
        Manifest.permission.RECEIVE_BOOT_COMPLETED,
        Manifest.permission.ANSWER_PHONE_CALLS,
        Manifest.permission.CALL_PHONE
    )
    private lateinit var foregroundServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
    }

    /*
        [권한 요청 팝업 수락 시, 권한 부여가 바로 진행되는 지에 대해]

        https://developer.android.com/training/permissions/requesting
        "When the user grants a permission to your app, the system sends your app a result intent
        that contains the permission grant. Your app cannot use the granted permission until the
        system delivers this result intent, which happens when the app resumes."

        즉, 권한 부여가 즉시 이루어지지 않으며, 앱이 다시 시작될 때까지 기다려야 한다.
     */
    private fun checkPermissions() {
        val permissionDeniedList = mutableListOf<String>()
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                permissionDeniedList.add(permission)
            }
        }
        if (permissionDeniedList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionDeniedList.toTypedArray(),
                PERMISSIONS_REQUEST_CODE
            )
        } else {
            startForegroundService()
        }
    }

    private fun startForegroundService() {
        foregroundServiceIntent = Intent(this, ForegroundService::class.java)
        startForegroundService(foregroundServiceIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                val permissionDeniedList = mutableListOf<String>()
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionDeniedList.add(permissions[i])
                    }
                }
                if (permissionDeniedList.isNotEmpty()) {
                    showPermissionAlertDialog(permissionDeniedList)
                } else {
                    startForegroundService()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showPermissionAlertDialog(permissionDeniedList: List<String>) {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("This app requires the following permissions: ${permissionDeniedList.joinToString()}")
            .setPositiveButton("Grant") { dialogInterface: DialogInterface, i: Int ->
                ActivityCompat.requestPermissions(
                    this,
                    permissionDeniedList.toTypedArray(),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(
                    this,
                    "The app cannot be used without granting the required permissions.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            .show()
    }
}