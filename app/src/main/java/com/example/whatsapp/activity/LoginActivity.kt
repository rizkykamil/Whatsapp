package com.example.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import com.example.whatsapp.activity.MainActivity
import com.example.whatsapp.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class LoginActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if (user != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)//menghilangkan Action Bar
        setContentView(R.layout.activity_login)

        setTextChangedListener(edt_email_login,til_email_login)
        setTextChangedListener(edt_password_login,til_password_login)
        progress_layout_login.setOnTouchListener{v, event ->true}

        txt_signup.setOnClickListener{//ketika text di click jadi ke fungsi on Signup
            onSignup()
        }
        btn_login.setOnClickListener{//ketika text di click jadi ke fungsi on login
            onLogin()
        }
    }

    private fun setTextChangedListener(edt: EditText, til: TextInputLayout) {
        edt.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?,
                                           start: Int, count: Int,
                                           after: Int) {

            }

            //ketika editText di ubah memastikan TextInput tidak meunjukan pesan error
            override fun onTextChanged(s: CharSequence?,
                                       start: Int, before: Int,
                                       count: Int) {
                til.isErrorEnabled = false

            }
        })

    }

    private fun onLogin(){
        var procceed= true
        if(edt_email_login.text.isNullOrEmpty()){        // ngecek jika edit text nya kosong gunakan Text Input Layout menampilkan pesan mengubah state til yang sebelomnya tidak menampilkan error sekarang menambilakan
            til_email_login.error = "Required Password"
            til_email_login.isErrorEnabled = true
            procceed = false
        }
        if (edt_password_login.text.isNullOrEmpty()) {
            til_password_login.error = "Required Password"
            til_password_login.isErrorEnabled = true
            procceed = false
        }

        if(procceed){
            progress_layout_login.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(
                edt_email_login.text.toString(),
                edt_password_login.text.toString()
            )
                .addOnCompleteListener { task->
                    if (!task.isSuccessful){
                        progress_layout_login.visibility  = View.GONE
                            Toast.makeText(
                                this@LoginActivity,
                                "login nya error boss: ${task.exception?.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }
                .addOnFailureListener {e -> // Jika proses sebelomnya tidak di laksanakan progresbar akan hilang ->  ditampilkan log errnya
                        progress_layout_login.visibility = View.GONE
                        e.printStackTrace()

                    }
                }
        }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)

    }
    private fun onSignup(){
        startActivity(Intent(this,SignUpActivity::class.java))
    }

}
