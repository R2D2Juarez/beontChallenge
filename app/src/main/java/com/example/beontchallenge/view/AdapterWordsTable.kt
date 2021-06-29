package com.example.beontchallenge.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beontchallenge.R

class AdapterWordsTable(var words: List<Pair<String, Int>>) :
    RecyclerView.Adapter<AdapterWordsTable.WordsViewHolder>() {

    fun updateWordsList(newList: List<Pair<String, Int>>) {
        words = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WordsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_words, parent, false)
    )


    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    class WordsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvWordName: TextView = view.findViewById(R.id.tv_wordName)
        private val tvConcurrencyNumber: TextView = view.findViewById(R.id.tv_concurrencyNumber)

        fun bind(wordRow: Pair<String, Int>) {
            tvWordName.text = wordRow.first
            tvConcurrencyNumber.text = wordRow.second.toString()
        }
    }
}