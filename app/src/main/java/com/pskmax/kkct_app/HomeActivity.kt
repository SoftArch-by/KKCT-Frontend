package com.pskmax.kkct_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.pskmax.kkct_app.data.Customer
import com.pskmax.kkct_app.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    private fun getFromBackEnd(email:String,pwd:String): MutableList<String>{
        //////// id,email,pwd,token ///////
        val element = mutableListOf<String>("1234567890",email,pwd,"1579900999999")
        return element
    }

    //กันไม่ให้ย้อนกลับ
    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            moveTaskToBack(true)
            finishAffinity()
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun changeFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment : Fragment = HomeFragment()
        val bundle = Bundle()

        var login_email:String = ""
        var login_pwd:String = ""
        var login_token:String = ""

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val user = Customer()

        //// รับค่าจาก LoadActivity ////
        intent.extras?.get("ui_email")?.let {
            login_email = it.toString()
        }
        intent.extras?.get("ui_pwd")?.let {
            login_pwd = it.toString()
        }
        intent.extras?.get("token")?.let {
            login_token = it.toString()
        }

        ////// api query ////
        user.fetchUserInfo(getFromBackEnd(login_email,login_pwd),login_token)
        println("${user.getUserId()} ${user.getUserEmail()} ${user.getUserPwd()} ${user.getUserCitizenId()} ${user.getUserToken()}")

        println("HomeAc token: " + login_token)

        bundle.putString("usEmail",login_email)
        bundle.putString("usPwd",login_pwd)
        bundle.putString("usToken",login_token)
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()

        binding.bottomNav.setOnItemSelectedListener {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment : Fragment
            val bundle = Bundle()
            when (it.itemId) {
                R.id.home -> {
                    fragment = HomeFragment()
                    bundle.putString("usEmail",login_email)
                    bundle.putString("usPwd",login_pwd)
                    bundle.putString("usToken",login_token)
                    fragment.arguments = bundle
                    fragmentTransaction.replace(R.id.frame_layout,fragment)
                    fragmentTransaction.commit()
                }
                R.id.log -> {
                    fragment = LogFragment()
                    bundle.putString("usEmail",login_email)
                    bundle.putString("usToken",login_token)
                    fragment.arguments = bundle
                    fragmentTransaction.replace(R.id.frame_layout,fragment)
                    fragmentTransaction.commit()
                }
                R.id.settings -> {
                    changeFragment(SettingsFragment())
                }
            }
            true
        }
    }
}