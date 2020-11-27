package com.android.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qw.view.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}