package com.example.vocabularyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyapp.databinding.ItemVocabularyBinding
import com.example.vocabularyapp.model.ListWordState
import com.example.vocabularyapp.model.WordData

class VocabAdapter(
    mList: List<WordData>,
    selectedListState: ListWordState,
    private val onRemoveItem: (Int) -> Unit
) : RecyclerView.Adapter<VocabAdapter.VocabViewHolder>() {
    private var currentList = mList
    private var currentListState = selectedListState

    class VocabViewHolder(private val itemVocabularyBinding: ItemVocabularyBinding) :
        RecyclerView.ViewHolder(itemVocabularyBinding.root) {
        fun bind(item: WordData, currentListState: ListWordState, onRemoveItem: (Int) -> Unit) {
            itemVocabularyBinding.tvNameVocab.text = item.name
            itemVocabularyBinding.tvMeaning.text = item.meaning
            itemVocabularyBinding.tvCategory.apply {
                text = item.category.title
                requestLayout()
            }
            itemVocabularyBinding.layoutCategory.setCardBackgroundColor(
                itemVocabularyBinding.root.context.getColor(
                    item.category.color
                )
            )
            itemVocabularyBinding.btnRemove.isVisible = currentListState == ListWordState.REMOVED
            itemVocabularyBinding.btnRemove.setOnClickListener {
                onRemoveItem(item.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabViewHolder {
        val view = ItemVocabularyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VocabViewHolder(view)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: VocabViewHolder, position: Int) {
        holder.bind(currentList[position], currentListState, onRemoveItem)
    }

    internal fun setListState(selectedListState: ListWordState) {
        currentListState = selectedListState
        notifyDataSetChanged()
    }

    internal fun refreshList(list: List<WordData>) {
        currentList = list
        notifyDataSetChanged()
    }
}