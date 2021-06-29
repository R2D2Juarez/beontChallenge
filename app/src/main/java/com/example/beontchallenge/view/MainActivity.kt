package com.example.beontchallenge.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.beontchallenge.databinding.ActivityMainBinding
import com.example.beontchallenge.viewmodel.CvsDataViewModel
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CvsDataViewModel
    private lateinit var input: InputStreamReader
    private lateinit var reader: BufferedReader
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var wordAdapter: AdapterWordsTable = AdapterWordsTable(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProviders.of(this).get(CvsDataViewModel::class.java)

        input = InputStreamReader(assets.open("test.csv"))
        reader = BufferedReader(input)

        coroutineScope.launch {
            val setDataTableDeferred =
                coroutineScope.async(Dispatchers.IO) { viewModel.setDataTable(reader) }
            setDataTableDeferred.await()
            updateUI()
        }
    }

    private fun updateUI() {
        binding.spinnerLoading.visibility = View.GONE
        binding.tvLoading.visibility = View.GONE
        binding.tvHeader.visibility = View.VISIBLE
        binding.tvHeader2.visibility = View.VISIBLE
        binding.rvWordsConcurrency.apply {
            adapter = wordAdapter
        }
        wordAdapter.updateWordsList(viewModel.counterTable)
    }
}