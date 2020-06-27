package dev.vedantm.samachar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dev.vedantm.samachar.R
import dev.vedantm.samachar.db.ArticleDatabase
import dev.vedantm.samachar.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_main.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
 val newsrepository=NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory=NewsViewModelProviderFactory(newsrepository)
        viewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }


}