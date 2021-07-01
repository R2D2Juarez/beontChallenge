package com.example.beontchallenge.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.beontchallenge.databinding.ActivityMainBinding
import com.example.beontchallenge.viewmodel.CvsDataViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CvsDataViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var wordAdapter: AdapterWordsTable = AdapterWordsTable(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProviders.of(this).get(CvsDataViewModel::class.java)

        binding.btnStart.setOnClickListener {
            binding.tvLoading.visibility = View.VISIBLE
            binding.pbDownloadProgress.visibility = View.VISIBLE
            binding.btnStart.visibility = View.GONE

            coroutineScope.launch {
                val setDataTableDeferred =
                    coroutineScope.async(Dispatchers.IO) { viewModel.setDataTable(this@MainActivity) }
                setDataTableDeferred.await()
                updateUI()
            }

            viewModel.mCounterTable.observe(this, {
                it?.let { wordsList ->
                    wordAdapter.updateWordsList(wordsList)
                }
            })
        }
    }

    private fun updateUI() {
        binding.pbDownloadProgress.visibility = View.GONE
        binding.tvLoading.visibility = View.GONE
        binding.tvHeader.visibility = View.VISIBLE
        binding.tvHeader2.visibility = View.VISIBLE
        binding.rvWordsConcurrency.apply {
            adapter = wordAdapter
        }
    }
}