package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class guruHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guru_home)

        val mFragmentManager=supportFragmentManager
        val mGuruHome=homeguru()

        mFragmentManager.findFragmentByTag(homeguru::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameGuruContainer,mGuruHome,homeguru::class.java.simpleName)
            .commit()
    }
}