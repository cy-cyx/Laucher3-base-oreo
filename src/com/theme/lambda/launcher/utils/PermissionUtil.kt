package com.theme.lambda.launcher.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.launcher3.BuildConfig
import com.android.launcher3.R

object PermissionUtil {
    private var sPermissionCallback: IPermissionCallback? = null
    private var sPermissions: Array<String>? = null

    private const val sCodeRequestPermission = 9999

    private var requestTime = 0

    private var force = true  // 强制，重复弹权限知道同意
    private var showGotoSetDialog = false

    fun requestRuntimePermissions(
        context: Context?,
        permissions: Array<String>?,
        permissionCallback: IPermissionCallback?,
        force: Boolean = true,
        showGotoSetDialog: Boolean = true
    ) {
        if (context == null || permissions == null) {
            return
        }
        this.force = force
        this.showGotoSetDialog = showGotoSetDialog
        sPermissionCallback = permissionCallback
        sPermissions = permissions
        requestTime = 0
        requestPermissions(context, sPermissions, true)
    }

    private fun requestPermissions(context: Context, permissions: Array<String>?, first: Boolean) {
        // 防止死循环
        if (requestTime > 5) {
            // 无权限
            sPermissionCallback?.noPermission()
            sPermissionCallback = null
            sPermissions = null
            return
        }

        val permissionList: MutableList<String> = ArrayList()
        for (permission in permissions!!) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.add(permission)
            }
        }
        // 申请权限
        if (permissionList.isNotEmpty()) {

            // 检查有没有拒接的权限
            val refusePermissionList: MutableList<String> = ArrayList()
            for (i in permissionList.indices) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        (context as Activity),
                        permissionList[i]
                    )
                ) {
                    refusePermissionList.add(permissionList[i])
                }
            }

            if (refusePermissionList.isNotEmpty() && !first) {

                // 特殊产品需求，不允许如果不是强制，就算了
                if (!force) {
                    if (!isHasRequest(refusePermissionList)) {
                        // 无权限
                        sPermissionCallback?.noPermission()
                        sPermissionCallback = null
                        sPermissions = null
                        return
                    }
                }

                if (showGotoSetDialog) {
                    val dialog = AlertDialog.Builder(context).apply {
                        setTitle(R.string.permission_refuse_tip)
                        setPositiveButton(R.string.goto_set,
                            object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    gotoSetPage(context)
                                }
                            })
                    }
                    dialog.show()

                    sPermissionCallback?.gotoSet(true)
                    sPermissionCallback = null
                    sPermissions = null
                } else {

                    sPermissionCallback?.gotoSet(false)
                    sPermissionCallback = null
                    sPermissions = null
                }
            } else {

                // 不强制询问一次就好
                if (!first && !force) {
                    // 无权限
                    sPermissionCallback?.noPermission()
                    sPermissionCallback = null
                    sPermissions = null
                    return
                }

                ActivityCompat.requestPermissions(
                    (context as Activity),
                    permissionList.toTypedArray(), sCodeRequestPermission
                )
            }

        } else {
            // 拥有权限
            sPermissionCallback?.nextStep()
            sPermissionCallback = null
            sPermissions = null
        }

        requestTime++
    }

    /**
     * 如果是不强制，首次拒接不直接去设置
     */
    private fun isHasRequest(permissions: MutableList<String>): Boolean {
        // 存在没有请求过的，就算没有请求过
        var has = true
        permissions.forEach {
            if (!"${SpKey.keyFirstCheckPermission}${it}".getSpBool()) {
                has = false
                "${SpKey.keyFirstCheckPermission}${it}".putSpBool(true)
            }
        }
        return has
    }

    fun onRequestPermissionsResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray?
    ) {
        if (requestCode == sCodeRequestPermission) {
            if (sPermissions != null) {
                requestPermissions(context, sPermissions, false)
            }
        }
    }

    fun gotoSetPage(context: Context) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.applicationContext.packageName, null)
        (context as Activity).startActivity(intent)
    }

    fun gotoNotifySetPage(context: Context) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra(
                "android.provider.extra.APP_PACKAGE",
                context.packageName
            )
        } else {
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun gotoAlarmPermissionSetPage(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uri = Uri.parse("package:" + context.packageName)
            val i = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, uri)
            context.startActivity(i)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun gotoFillAccessPage(context: Context) {
        if (Environment.isExternalStorageManager()) return
        try {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            context.startActivity(intent)
        } catch (ex: Exception) {
            val intent = Intent()
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            context.startActivity(intent)
        }

    }

    interface IPermissionCallback {
        fun nextStep()

        fun noPermission()

        fun gotoSet(internal: Boolean)
    }
}