package com.example.beontchallenge.viewmodel

import androidx.lifecycle.ViewModel
import com.example.beontchallenge.model.Word
import java.io.BufferedReader

class CvsDataViewModel : ViewModel() {

    var counterTable = arrayListOf<Pair<String, Int>>()
    var wordsHashMap = hashMapOf<String, Int>()

    fun setDataTable(reader: BufferedReader) {

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            line?.let {
                val words: List<String> = it.split(" ")
                for (word in words) {
                    word.lowercase()
                    if (wordsHashMap.containsKey(word)) {
                        wordsHashMap[word]?.let { helper ->
                            wordsHashMap[word] = helper + 1
                        }
                    } else {
                        wordsHashMap[word] = if (word.isNotEmpty()) 1 else 0
                    }
                }
            }
        }

        val sortedMap: MutableMap<String, Int> = LinkedHashMap()
        wordsHashMap.entries.sortedByDescending { it.value }
            .forEach { sortedMap[it.key] = it.value }

        for (data in sortedMap) {
            counterTable.add(Pair(data.key, data.value))
        }
    }
}