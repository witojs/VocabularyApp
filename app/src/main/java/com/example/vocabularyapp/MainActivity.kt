package com.example.vocabularyapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.vocabularyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ///getting username from shared preference
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("USER_NAME", null)
        if (savedUsername != null) {
            //direct to main page
            navigateToDashboard(savedUsername)
        } else {
            showOnboard()
        }
    }

    ///listen to et change and set title
    private fun showOnboard() {
        binding.etNameOnboarding.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.tvTitle.text = getString(R.string.txt_title_default)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvTitle.text = getString(R.string.txt_title_replace)
            }

            override fun afterTextChanged(s: Editable?) {
                binding.tvTitle.text = getString(R.string.txt_title_replace)
                binding.btnStart.isVisible = !s.isNullOrEmpty()
            }
        })

        binding.btnStart.setOnClickListener {
            val username = binding.etNameOnboarding.text.toString()
            saveName(username)
            //go to next screen
            navigateToDashboard(username)
        }
    }

    ///save name to shared preference
    private fun saveName(username: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_NAME", username)
        editor.apply()
    }

    private fun navigateToDashboard(username: String) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("USER_NAME", username)
        startActivity(intent)
    }
}