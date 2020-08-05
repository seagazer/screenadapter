package com.seagazer.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seagazer.screenadapter.ScreenAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ScreenAdapter.adjustWidthDensity(this, application)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}