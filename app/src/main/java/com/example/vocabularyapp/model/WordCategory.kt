package com.example.vocabularyapp.model

import androidx.annotation.ColorRes
import com.example.vocabularyapp.R

enum class WordCategory(val title: String, @ColorRes val color: Int) {
    ALL_CATEGORY("All Categories", R.color.black),
    ADJECTIVE("Adjective", R.color.green),
    PREPOSITION("Preposition", R.color.red),
    VERB("Verb", R.color.yellow),
    NOUN("Noun", R.color.purple)
}

enum class ListWordState {
    NORMAL,
    REMOVED
}