package com.example.nybooks.presentation.books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nybooks.R
import com.example.nybooks.data.repository.BooksApiDataSource
import com.example.nybooks.presentation.base.BaseActivity
import com.example.nybooks.presentation.books.BooksAdapter.RecyclerViewClickListener
import com.example.nybooks.presentation.details.BookDetailsActivity
import kotlinx.android.synthetic.main.activity_books.*
import kotlinx.android.synthetic.main.include_toolbar.*


class BooksActivity : BaseActivity(), RecyclerViewClickListener {

    private lateinit var viewModel : BooksViewModel
    private lateinit var booksAdapter: BooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        setupToolbar(toolbarMain, R.string.books_title)

        viewModel = BooksViewModel.ViewModelFactory(BooksApiDataSource(application)).create(BooksViewModel::class.java)

        viewModel.booksLiveData.observe(this, Observer {
            it?.let { books ->
                with(recyclerBooks) {
                    layoutManager = LinearLayoutManager(this@BooksActivity, RecyclerView.VERTICAL, false)
                    setHasFixedSize(true)
                    booksAdapter = BooksAdapter(books, this@BooksActivity)
                    recyclerBooks.adapter = booksAdapter
                }
            }
        })

        viewModel.viewFlipperLiveData.observe(this, Observer {
            it?.let { viewFlipper ->
                viewFlipperBooks.displayedChild = viewFlipper.first

                viewFlipper.second?.let { errorMessageResId ->
                    textViewError.text = getString(errorMessageResId)
                }
            }
        })

        viewModel.reviewsLiveData.observe(this, Observer { reviewsDetailsResponse ->
            reviewsDetailsResponse?.let {
                it.forEach { reviewDetail ->
                    Log.e("summary", reviewDetail.summary)
                }
            }
        })

        viewModel.getBooks()
        viewModel.getReviewByAuthor()
    }

    override fun onClick(view: View?, position: Int) {
        val intent = BookDetailsActivity.getStartIntent(this,
            viewModel.booksLiveData.value?.get(position)?.title!!,
            viewModel.booksLiveData.value?.get(position)?.description!!)
        startActivity(intent)
    }
}
