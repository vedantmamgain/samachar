package dev.vedantm.samachar.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
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

class fragment_breaking :Fragment(R.layout.fragment_breaking){
val TAG="Breaking NEWs"
    lateinit var viewModel:NewsViewModel
     lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Sucess ->{
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / 20 + 2
                        isLoading =viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG,it)
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }



        })
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle =Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_fragment_breaking_to_fragment_article,
                bundle
                )
        }
    }
    var isLoading=false
    var isLastPage=false
    var isScrolling=false
    var scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition =layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPage =!isLoading && !isLastPage
            val isAtLastItem=firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling=false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }
    }




    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@fragment_breaking.scrollListener)
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