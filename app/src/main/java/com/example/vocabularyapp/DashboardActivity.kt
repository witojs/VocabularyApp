package com.example.vocabularyapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyapp.adapter.CategoryAdapter
import com.example.vocabularyapp.adapter.VocabAdapter
import com.example.vocabularyapp.databinding.ActivityDashboardBinding
import com.example.vocabularyapp.db.SqlDbHandler
import com.example.vocabularyapp.model.ListWordState
import com.example.vocabularyapp.model.WordCategory

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var adapterCategory: CategoryAdapter
    private lateinit var adapterVocab: VocabAdapter
    private var selectListState: ListWordState = ListWordState.NORMAL
    private var selectedCategory: WordCategory = WordCategory.ALL_CATEGORY
    private val dbHandler: SqlDbHandler = SqlDbHandler(this)
    private var progress = 0
    private var maxVocab = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ///get username from shared preference
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("USER_NAME", null)
        if (username != null) {
            binding.tvGreeting.text = getString(R.string.txt_greeting, username)
        }
        buttonDeleteAdd()
        setCategoryList()
        setVocabList()
        setProgress()

        //add vocab
        binding.ivAdd.setOnClickListener {
            navigateToNewVocab()
        }

        //delete vocab
        binding.ivDelete.setOnClickListener {
            buttonCancel()
            selectListState = ListWordState.REMOVED
            adapterVocab.setListState(selectListState)
        }

        //cancel
        binding.btnCancel.setOnClickListener {
            buttonDeleteAdd()
            selectListState = ListWordState.NORMAL
            adapterVocab.setListState(selectListState)
        }
    }

    ///set category Adapter
    fun setCategoryList() {
        val categoryList = WordCategory.values().toList()
        adapterCategory = CategoryAdapter(categoryList, selectedCategory) { wordCategory ->
            selectedCategory = wordCategory
            refreshCategoryAndVocab(wordCategory)
        }

        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = adapterCategory
        }

     }

    private fun refreshCategoryAndVocab(wordCategory: WordCategory) {
        val listWord = if (wordCategory == WordCategory.ALL_CATEGORY) {
            dbHandler.getVocab()
        } else {
            dbHandler.getVocab().filter { it.category == wordCategory }
        }
        //update list
        adapterVocab.refreshList(listWord)
        adapterCategory.updateSelectedCategory(selectedCategory)
    }

    //navigate to add new vocab
    private fun navigateToNewVocab() {
        val intent = Intent(this, AddActivity::class.java)
        startActivityForResult(intent, 123)
    }

    //set value vocab adapter
    private fun setVocabList() {
        adapterVocab = VocabAdapter(dbHandler.getVocab(), selectListState) { positionToBeRemoved ->
            //remove and refresh
            removeAndRefresh(positionToBeRemoved)
        }
        binding.rvVocab.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = adapterVocab
        }
    }

    //remove and refresh vocab adapter
    private fun removeAndRefresh(position: Int) {
        dbHandler.deleteVocab(position)
        adapterVocab.refreshList(dbHandler.getVocab())
        setProgress()
        //mengatur button yang tampil
        if (dbHandler.getVocab().isNotEmpty()) {
            buttonCancel()
        } else {
            buttonDeleteAdd()
        }
    }

    private fun buttonCancel() {
        binding.btnCancel.isVisible = true
        binding.ivDelete.isVisible = false
        binding.ivAdd.isVisible = false
        setProgress()
    }

    private fun buttonDeleteAdd() {
        binding.btnCancel.isVisible = false
        binding.ivAdd.isVisible = true
//        binding.ivDelete.isVisible = dbHandler.getVocab().isNotEmpty()
        binding.ivDelete.isVisible = true
        setProgress()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 123) {
            adapterVocab.refreshList(dbHandler.getVocab())
            buttonDeleteAdd()
        }
    }

    private fun setProgress() {
        progress = (dbHandler.getVocab().size * 100) / maxVocab
        binding.tvTitleVocabAvailableValue.text = getString(R.string.txt_available_value, progress)
        binding.tvAchieved.text = "$progress %"
        binding.pbAchieved.progress = progress
//        binding.ivAdd.isVisible = progress < 100 && selectListState == ListWordState.NORMAL

    }
}