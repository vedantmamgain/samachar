package dev.vedantm.samachar.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.vedantm.samachar.R
import dev.vedantm.samachar.adapters.NewsAdapter
import dev.vedantm.samachar.ui.NewsActivity
import dev.vedantm.samachar.ui.NewsViewModel
import dev.vedantm.samachar.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking.*
import kotlinx.android.synthetic.main.fragment_search.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class fragment_search :Fragment(R.layout.fragment_search){
    val TAG="Breaking News"
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        var job: Job? = null
        etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
            }
            it?.let {
                if (it.toString().isNotEmpty()) {
                    viewModel.searchNews(it.toString())
                }
            }
        }
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_fragment_search_to_fragment_article,
                bundle
            )
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer
        { response ->
            when (response) {
                is Resource.Sucess -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / 20 + 2
                        isLoading = viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG, it)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }



        var isLoading = false
        var isLastPage = false
        var isScrolling = false
        var scrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
                val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                val isNotAtBeginning = firstVisibleItemPosition >= 0
                val isTotalMoreThanVisible = totalItemCount >= 20
                val shouldPaginate =
                    isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
                if (shouldPaginate) {
                    viewModel.searchNews(etSearch.text.toString())
                    isScrolling = false
                }

            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        }


    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@fragment_search.scrollListener)
        }
    }
    private fun hideProgressBar(){
        paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar(){
        paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

}