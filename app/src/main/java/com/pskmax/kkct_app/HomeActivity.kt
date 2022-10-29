package com.pskmax.kkct_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {

    var profileCardView: CardView? = null
    var creditCardView: CardView? = null
    var logCardView: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()


        profileCardView = findViewById<CardView>(R.id.profileCardView)
        creditCardView = findViewById<CardView>(R.id.creditCardView)
        logCardView = findViewById<CardView>(R.id.logCardView)

        profileCardView!!.setOnClickListener{
            val intent = Intent(this@HomeActivity,ProfileActivity::class.java)
            startActivity(intent)
        }

        creditCardView!!.setOnClickListener{
            val intent = Intent(this@HomeActivity,CreditActivity::class.java)
            startActivity(intent)
        }

        logCardView!!.setOnClickListener{
            val intent = Intent(this@HomeActivity,LogActivity::class.java)
            startActivity(intent)
        }

    }
}