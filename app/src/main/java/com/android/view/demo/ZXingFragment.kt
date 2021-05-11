package com.android.view.demo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.view.databinding.ViewZxingFragmentBinding
import com.android.view.demo.zxing.CaptureQRCodeActivity
import com.idaddy.android.zxing.Intents
import com.qw.framework.core.ui.BaseFragment

/**
 * Created by qinwei on 1/20/21 7:19 PM
 * email: qinwei_it@163.com
 */
class ZXingFragment : BaseFragment() {
    private lateinit var binding: ViewZxingFragmentBinding

    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ViewZxingFragmentBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun initView(view: View) {
        binding.mActivityScannerBtn.setOnClickListener {
            startActivityForResult(Intent(context, CaptureQRCodeActivity::class.java), 100)
        }
        binding.mFragmentScannerBtn.setOnClickListener {

        }
    }

    override fun initData() {
        requestPermissions()
    }

    private fun requestPermissions() {
        val result = ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
        if (result != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 200)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 200) {
            if (grantResults.any { i -> i == PackageManager.PERMISSION_GRANTED }) {
                AlertDialog.Builder(context).setTitle("权限")
                    .setMessage("需要相机权限")
                    .setCancelable(false)
                    .setNegativeButton(
                        "申请"
                    ) { dialog, which -> requestPermissions() }
                    .setPositiveButton(
                        "退出"
                    ) { dialog, which -> activity?.finish() }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                val result = data?.getStringExtra(Intents.Scan.RESULT)
                Toast.makeText(context, result ?: "", Toast.LENGTH_SHORT).show()
            }
        }
    }
}