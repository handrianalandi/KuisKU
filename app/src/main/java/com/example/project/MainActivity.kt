package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore


var db = FirebaseFirestore.getInstance()
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val mFragmentManager=supportFragmentManager
//        val mLogin=loginpage()
//
//        mFragmentManager.findFragmentByTag(loginpage::class.java.simpleName)
//        mFragmentManager
//            .beginTransaction()
//            .add(R.id.frameContainer,mLogin,loginpage::class.java.simpleName)
//            .commit()
    }

    override fun onStart() {
        super.onStart()
        val mFragmentManager=supportFragmentManager
        val mLogin=loginpage()

        mFragmentManager.findFragmentByTag(loginpage::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer,mLogin,loginpage::class.java.simpleName)
            .commit()
    }
}