package com.pskmax.kkct_app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterActivity : AppCompatActivity() {

    var editEmail: EditText? = null
    var editPassword: EditText? = null
    var editCPassword: EditText? = null
    var editId: EditText? = null
    var btnRegister: Button? = null
    var btnToLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        editEmail = findViewById<EditText>(R.id.editEmail)
        editPassword = findViewById<EditText>(R.id.editPassword)
        editCPassword = findViewById<EditText>(R.id.editCPassword)
        editId = findViewById<EditText>(R.id.editId)
        btnRegister = findViewById<Button>(R.id.btnRegister)
        btnToLogin = findViewById<Button>(R.id.btnToLogin)

        btnRegister!!.setOnClickListener{
            // check email and password
            if (editPassword?.length()!! < 8){}
            else if(editPassword?.length()!! > 15){}
            //else if(editPassword!! != editCPassword!!){}
            // if correct
            else {
                val intent = Intent(this@RegisterActivity,HomeActivity::class.java)
                startActivity(intent)
            }
        }

        btnToLogin!!.setOnClickListener{
            val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}