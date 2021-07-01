package com.example.beontchallenge.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.File

class CvsDataViewModel : ViewModel() {

    private var wordsHashMap = hashMapOf<String, Int>()
    var mCounterTable = MutableLiveData<ArrayList<Pair<String, Int>>>()
    var mCounterProgress = MutableLiveData<ArrayList<Int>>()

    private var URL = "https://www.dropbox.com/s/frkejggpxyklc68/test.csv?dl=1"
    
    init {
        mCounterTable.value = arrayListOf()
        mCounterProgress.value = arrayListOf()
    }

    fun setDataTable(context: Context) {

        //region DOWNLOAD THE FILE
        val request = DownloadManager.Request(Uri.parse(URL))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setTitle("BeontTestFile")
            .setDescription("File is downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            .setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOCUMENTS,
                "BeontTestFile.csv"
            )

        val dm = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

        val input = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/BeontTestFile.csv").toString()).inputStream()
        val reader = BufferedReader(input.reader())
        //endregion

        //region HANDLE THE DOWNLOADED DATA
        val listOfRows = reader.readLines()

        listOfRows.forEachIndexed { index, row ->

            val words: List<String> = row.split(" ")
            for (word in words) {
                word.lowercase()
                handleWordWithHelper(word)
            }
        }
        sortWordsByConcurrency()
        //endregion
    }

    private fun handleWordWithHelper(word: String) {
        if (wordsHashMap.containsKey(word)) {
            wordsHashMap[word]?.let { helper ->
                wordsHashMap[word] = helper + 1
            }
        } else {
            wordsHashMap[word] = if (word.isNotEmpty()) 1 else 0
        }
    }

    private fun sortWordsByConcurrency() {
        val sortedMap: MutableMap<String, Int> = LinkedHashMap()
        wordsHashMap.entries.sortedByDescending { it.value }
            .forEach { sortedMap[it.key] = it.value }

        for (data in sortedMap) {
            mCounterTable.value?.add(Pair(data.key, data.value))
        }
    }
}