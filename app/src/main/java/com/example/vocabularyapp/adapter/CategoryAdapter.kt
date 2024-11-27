package com.example.vocabularyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyapp.R
import com.example.vocabularyapp.databinding.ItemCategoryBinding
import com.example.vocabularyapp.model.WordCategory

class CategoryAdapter(
    private val mList: List<WordCategory>,
    selectedCategory: WordCategory,
    private var onSelectedCategory: (WordCategory) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var currentCategory: WordCategory = selectedCategory

    class CategoryViewHolder(private val itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun bind(
            item: WordCategory,
            selectedCategory: WordCategory,
            onSelectedCategory: (WordCategory) -> Unit
        ) {
            itemCategoryBinding.tvCategory.text = item.title
            if (item.ordinal == selectedCategory.ordinal) {
                itemCategoryBinding.border.setCardBackgroundColor(
                    itemCategoryBinding.root.context.getColor(
                        R.color.colorBackgroundStartButton
                    )
                )
            } else {
                itemCategoryBinding.border.setCardBackgroundColor(
                    itemCategoryBinding.root.context.getColor(
                        R.color.colorCardBackground
                    )
                )
            }
            itemCategoryBinding.root.setOnClickListener {
                onSelectedCategory(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(mList[position], currentCategory, onSelectedCategory)
    }

    internal fun updateSelectedCategory(selectedCategory: WordCategory) {
        notifyItemChanged(currentCategory.ordinal)
        currentCategory = selectedCategory
        notifyItemChanged(selectedCategory.ordinal)
    }
}