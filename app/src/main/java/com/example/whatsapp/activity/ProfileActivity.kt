package com.example.whatsapp.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.whatsapp.R
import com.example.whatsapp.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDb = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val firebaseStorage = FirebaseStorage.getInstance().getReference()
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if(userId.isNullOrEmpty()){
            finish()
        }
        progressBar.setOnTouchListener{v,event->true}
        btn_apply.setOnClickListener {
            onApply()
        }
        btn_delete_account.setOnClickListener {
            onDelete()
        }
        populateInfo()

        imbtn_profile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,REQUEST_CODE_PHOTO)
        }

    }

    override fun onResume() {
        super.onResume()
        if (firebaseAuth.currentUser==null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK&& requestCode == REQUEST_CODE_PHOTO){
            storeImage(data?.data)
        }
    }

    private fun storeImage(uri: Uri?) {
        if (uri != null){
            Toast.makeText(this,"SABAR BOSS LAGI UPLOAD",Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.VISIBLE
            val filePath = firebaseStorage.child(DATA_IMAGES).child(userId!!)

            filePath.putFile(uri)
                .addOnSuccessListener{
                    filePath.downloadUrl
                        .addOnSuccessListener{
                            val url = it.toString()
                            firebaseDb.collection(DATA_USERS)
                                .document(userId)
                                .update(DATA_USER_IMAGE_URL,url)
                                .addOnSuccessListener {
                                    imageUrl= url
                                    populateImage(this,imageUrl,img_profile,R.drawable.ic_user)
                                }
                            progressBar.visibility = View.GONE
                        }
                        .addOnFailureListener{
                            onUploadFailured()
                        }
                }
                .addOnFailureListener(){
                    onUploadFailured()
                }

        }
    }

    private fun onUploadFailured() {
        Toast.makeText(this,"gambar lu jelek , cari yang cakepan ",Toast.LENGTH_SHORT).show()
    }

    private fun populateInfo() {
        progressBar.visibility = View.VISIBLE
        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener {
                val user = it.toObject(Model::class.java)
                imageUrl= user?.imageUrl
                edt_name_profile.setText(user?.name,TextView.BufferType.EDITABLE)
                edt_status_profile.setText(user?.info,TextView.BufferType.EDITABLE)
                edt_phone_profile.setText(user?.phone,TextView.BufferType.EDITABLE)
                progressBar.visibility = View.GONE
                if (imageUrl != null) { // jika imageUrl tidak null, gambar dipasangkan ke imageview
                    populateImage(this, user?.imageUrl, img_profile, R.drawable. ic_user) }

            }
            .addOnFailureListener {e->
                e.printStackTrace()
                finish()
            }

    }

    private fun onDelete() {

        progressBar.visibility = View.GONE
        AlertDialog.Builder(this)
            .setTitle("DELETE ACCOUNT")
            .setMessage("YAKEN LU APUS, GAK NYESEL NICH")
            .setPositiveButton("Yes"){
                dialog, which -> firebaseDb.collection(DATA_USERS).document(userId!!).delete()
                firebaseAuth.currentUser?.delete()// perintah menghapus userId yang sedang digunakan
                    ?.addOnSuccessListener { finish()
                    }
                    ?.addOnFailureListener { finish()
                    }
                progress_layout.visibility= View.GONE
                Toast.makeText(this,"PROPIL UDAH KE APUS JAN NYESEL",Toast.LENGTH_SHORT)
                finish()
            }
            .setNegativeButton("No"){
                dialog, which ->
                progress_layout.visibility = View.GONE
            }
            .setCancelable(false)
            .show()
    }

    fun onApply() {
        progressBar.visibility= View.VISIBLE
        val name = edt_name_profile.text.toString()
        val info = edt_status_profile.text.toString()
        val phone = edt_phone_profile.text.toString()
        val map = HashMap<String,Any>()
        map[DATA_USER_NAME] = name
        map[DATA_USER_INFO]= info
        map[DATA_USER_PHONE]= phone

        firebaseDb.collection(DATA_USERS).document(userId!!).update(map)
            .addOnSuccessListener {
                Toast.makeText(this,"HOREEE UPDATE SUCCESS",Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {e ->
                e.printStackTrace()
                Toast.makeText(this,"Yah ganti hp sana",Toast.LENGTH_SHORT).show()
                progress_layout.visibility = View.GONE
            }
    }
}