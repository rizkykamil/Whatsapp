package com.example.whatsapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsapp.R
import com.example.whatsapp.model.DATA_USERS
import com.example.whatsapp.model.Model
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private val firebaseDb = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) // tidak ada yang atasnya (ijo) itu looh
        setContentView(R.layout.activity_sign_up)

        setTextChangedListener(edt_email_signup, til_email_signup)
        setTextChangedListener(edt_password_signup, til_password_signup)
        setTextChangedListener(edt_name_signup, til_name_signup)
        setTextChangedListener(edt_phone_signup, til_phone)
        progress_signup.setOnTouchListener { v, event -> true }

        btn_signup.setOnClickListener {
            onSignUp()
        }
        txt_login_signup.setOnClickListener {
            onLogin()
        }
    }

    private fun setTextChangedListener(
        edt: EditText,
        til: TextInputLayout
    ) {// bacanya edt itu editText loh, til itu Textinputlyout gitu looh
        edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                til.isErrorEnabled = false
            }

        })
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)


    }

    private fun onSignUp() {
        var proceed = true
        if (edt_name_signup.text.isNullOrEmpty()) {
            til_name_signup.error = "Required Name"
            til_name_signup.isErrorEnabled = true
            proceed = false
        }
        if (edt_phone_signup.text.isNullOrEmpty()) {
            til_phone.error = "Required Phone Number"
            til_phone.isErrorEnabled = true
            proceed = false
        }
        if (edt_email_signup.text.isNullOrEmpty()) {
            til_email_signup.error = "Required Password"
            til_email_signup.isErrorEnabled = true
            proceed = false
        }
        if (edt_password_signup.text.isNullOrEmpty()) {
            til_password_signup.error = "Required Password"
            til_password_signup.isErrorEnabled = true
            proceed = false
        }

        if (proceed) {
            progress_signup.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(
                edt_email_signup.text.toString(), edt_password_signup.text.toString()
            ).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    progress_signup.visibility = View.GONE
                    Toast.makeText(
                        this@SignUpActivity, "error nih boss: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (firebaseAuth.uid != null) {
                    val email = edt_email_signup.text.toString()
                    val phone = edt_phone_signup.text.toString()
                    val name = edt_name_signup.text.toString()
                    val user = Model(email, phone, name, "", "hallo gw baru nich", "", "")
                    firebaseDb.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)
                }
                progress_signup.visibility = View.GONE
            }
                .addOnFailureListener {
                    progress_signup.visibility = View.GONE
                    it.printStackTrace()
                }
        }
    }

    private fun onLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}