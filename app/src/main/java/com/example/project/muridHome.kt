package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class muridHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_murid_home)

        val mFragmentManager=supportFragmentManager
        val mMuridHome=muridHomeFragment()

        mFragmentManager.findFragmentByTag(muridHomeFragment::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameSiswaContainer,mMuridHome,muridHomeFragment::class.java.simpleName)
            .commit()
    }
}