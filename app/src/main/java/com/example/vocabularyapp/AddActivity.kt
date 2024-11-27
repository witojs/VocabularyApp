package com.example.vocabularyapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vocabularyapp.databinding.ActivityAddBinding
import com.example.vocabularyapp.db.SqlDbHandler
import com.example.vocabularyapp.model.WordCategory

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private val sqlDbHandler: SqlDbHandler = SqlDbHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ///set spinner dropdown
        setSpinner()

        /// adding vocab to sql database
        binding.btnSave.setOnClickListener {
            if (binding.etName.text.isNullOrEmpty()
                || binding.etMeaning.text.isNullOrEmpty()
                || binding.spinnerCategory.selectedItem.toString().isEmpty()
            ) return@setOnClickListener
            sqlDbHandler.addVocab(
                binding.etName.text.toString(),
                binding.etMeaning.text.toString(),
                binding.spinnerCategory.selectedItem.toString()
            )
            setResult(123, Intent())
            finish()
        }

        binding.btnDiscard.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun setSpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, getCategoryList()
        )
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    /// set dropdown value
    private fun getCategoryList(): List<String> {
        return WordCategory.values().map {
            if (it == WordCategory.ALL_CATEGORY) {
                ""
            } else {
                it.title
            }
        }
    }
}