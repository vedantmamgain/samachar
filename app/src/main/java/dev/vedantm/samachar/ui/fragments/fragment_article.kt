package dev.vedantm.samachar.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dev.vedantm.samachar.R
import dev.vedantm.samachar.ui.NewsActivity
import dev.vedantm.samachar.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class fragment_article :Fragment(R.layout.fragment_article){
    lateinit var viewModel: NewsViewModel
    val args:fragment_articleArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        val article=args.article
         webView.apply {
         webViewClient= WebViewClient()
         loadUrl(article.url)
    }
fab.setOnClickListener {
    viewModel.saveArticle(article)
    Snackbar.make(view,"Article Saved succesfully",Snackbar.LENGTH_SHORT).show()
}
    }
}