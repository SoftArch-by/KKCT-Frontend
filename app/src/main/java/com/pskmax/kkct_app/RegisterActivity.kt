package com.pskmax.kkct_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.pskmax.kkct_app.data.Register
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    var editEmail: EditText? = null
    var editPassword: EditText? = null
    var editCPassword: EditText? = null
    var editId: EditText? = null
    var btnRegister: Button? = null
    var btnToLogin: Button? = null
    var url:String?= null

    private fun checkPassword(string: String) : Boolean{
        val (letters , numeric) = string.partition { it.isLetter() }
        val (upperCases, lowerCases) = letters.partition { it.isUpperCase() }
        if (upperCases.isNotEmpty() && lowerCases.isNotEmpty() && numeric.isNotEmpty()){
            return  true
        }
        return  false
    }

    private val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    private fun isEmailValid(email: String): Boolean {
        return EMAIL_REGEX.toRegex().matches(email);
    }


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

        val regScreen = Register()
        btnRegister!!.setOnClickListener{

            if (!isEmailValid((editEmail?.text).toString())){
                println("Your Email is not correct")
            }
            else if (editPassword?.length()!! < 8){
                println("Your pass must be between 8-15 characters")
            }
            else if (!checkPassword((editPassword?.text).toString())){
                println("Your password must have at least 1 Uppercase, Lowercase and Numeric")
            }
            else if ((editPassword?.text).toString() != (editCPassword?.text).toString()){
                println("Your confirm password is not correct")
            }
            else if (editId?.length()!! < 13){
                println("Your Citizen ID must have 13 characters")
            }
            else{
                // ใช้ api ที่ back
                signUp(editEmail.toString(),editPassword.toString(), editId.toString())

                val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                regScreen.updateUserInfo((editEmail?.text).toString(),(editPassword?.text).toString(),(editId?.text).toString())
                // ลบ Stack ของ Intent///////////////
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish();
                /////////////////////////////////////
            }
        }

        btnToLogin!!.setOnClickListener{

            val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(email: String  ,password : String , Cid: String){

        val regJson = JSONObject()
        // รอค่า key ที่ database ที่ถูกต้องอีกที
        regJson.put("email",email)
        regJson.put("password",password)
        regJson.put("citizenID", Cid)
        // 10.0.2.2 คือค่า loopback ของ android studio , 8080 คือ port
        val url = "http://10.0.2.2:8080/api/signup"
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, url, regJson,
            Response.Listener{
                response -> Log.d("Respond",response.toString())
            },
            Response.ErrorListener{ error ->
                Log.d("Response",error.toString())
                return@ErrorListener
            }
        ){
            override fun getBodyContentType(): String {
                return "application/json"
            }

          }

        val queue = Volley.newRequestQueue(this)
        queue.add(jsonRequest)
    }

}