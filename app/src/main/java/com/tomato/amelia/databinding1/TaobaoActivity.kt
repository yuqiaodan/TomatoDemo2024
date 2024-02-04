package com.tomato.amelia.databinding1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tomato.amelia.R

class TaobaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taobao)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, OnSellFragment()).commit()
    }
}