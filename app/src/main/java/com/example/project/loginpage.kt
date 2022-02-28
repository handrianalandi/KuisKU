package com.example.project

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.util.Patterns

import android.text.TextUtils
import android.widget.TextView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [loginpage.newInstance] factory method to
 * create an instance of this fragment.
 */
class loginpage : Fragment() {
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loginpage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val btnRegister = view.findViewById<AppCompatButton>(R.id.btnRegister)
        val btnLogin = view.findViewById<AppCompatButton>(R.id.btnLogin)
        val _etemail = view.findViewById<EditText>(R.id.etEmail)
        val _etPassword = view.findViewById<EditText>(R.id._password)
        val _tvForgotPass=view.findViewById<TextView>(R.id.tvForgotPass)
        btnRegister.setOnClickListener {
            val mRegister = registerpage()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer, mRegister, registerpage::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        btnLogin.setOnClickListener {
            var lengkap = true
            if (_etemail.text.isEmpty()) {
                _etemail.error = "Email Tidak Boleh Kosong!"
                lengkap = false
            }
            if (_etPassword.text.isEmpty()) {
                _etPassword.error = "Password Tidak Boleh Kosong!"
                lengkap = false
            }
            if (lengkap) {
                val email = _etemail.text.toString()
                val password = _etPassword.text.toString()
                if(isValidEmail(email)){
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            //cek murid atau guru
                            val uid = it.user?.uid.toString()
                            db.collection("tbUser").get()
                                .addOnSuccessListener { result ->
                                    for (user in result) {
                                        if (user.data.get("uid").toString() == uid) {
                                            if (user.data.get("isteacher") == "1") {
                                                val intent = Intent(activity, guruHome::class.java)
                                                startActivity(intent)
                                            } else {
                                                val intent = Intent(activity, muridHome::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }
                        }
                        .addOnFailureListener{
                            Toast.makeText(activity,"Email / Password Salah!",Toast.LENGTH_SHORT).show()
                        }
                }
                else{
                    _etemail.error="Masukan Email Dengan Benar!"
                }
            }
        }
        _tvForgotPass.setOnClickListener {
            val mForgotPassword = resetpassword()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer, mForgotPassword, resetpassword::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser) {
        db.collection("tbUser")
            .get()
            .addOnSuccessListener { result ->
                for (user in result) {
                    if (user.data.get("UID").toString() == currentUser.uid) {
                        if (user.data.get("teacher") == "1") {
                            val intent = Intent(activity, guruHome::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(activity, muridHome::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment loginpage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            loginpage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}