package dev.vedantm.samachar.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.vedantm.samachar.R
import dev.vedantm.samachar.adapters.NewsAdapter
import dev.vedantm.samachar.ui.NewsActivity
import dev.vedantm.samachar.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_saved.*

class fragment_saved :Fragment(R.layout.fragment_saved){
    lateinit var viewModel: NewsViewModel
 lateinit var newsAdapter:NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle =Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_fragment_saved_to_fragment_article,
                bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return true;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
              val position=viewHolder.adapterPosition
                val article=newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Successfully deleted article",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }
ItemTouchHelper(itemTouchHelperCallback).apply {
    attachToRecyclerView(rvSavedNews)
}
viewModel.getSavedNews().observe(viewLifecycleOwner , Observer { articles->
    newsAdapter.differ.submitList(articles)
})


    }
    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }

    }
}