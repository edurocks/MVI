package com.example.nybooks.presentation.books


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nybooks.R
import com.example.nybooks.data.model.Book
import com.example.nybooks.data.model.BooksResult
import com.example.nybooks.data.model.ReviewsResult
import com.example.nybooks.data.repository.BooksRepository
import com.example.nybooks.data.response.ReviewDetailsResponse

@Suppress("UNCHECKED_CAST")
class BooksViewModel(private val dataSource: BooksRepository) : ViewModel() {

    companion object {
        private const val VIEW_FLIPPER_BOOKS = 1
        private const val VIEW_FLIPPER_ERROR = 2
    }

    val booksLiveData: MutableLiveData<List<Book>> = MutableLiveData()
    val viewFlipperLiveData: MutableLiveData<Pair<Int, Int?>> = MutableLiveData()
    private val _serverErrorMessage: MutableLiveData<Int> = MutableLiveData()
    val serverErrorMessage : LiveData<Int>
        get() = _serverErrorMessage
    private val _apiErrorMessage: MutableLiveData<Int> = MutableLiveData()
    val apiErrorMessage : LiveData<Int>
        get() = _apiErrorMessage
    private val _reviewsLiveData: MutableLiveData<List<ReviewDetailsResponse>> = MutableLiveData()
    val reviewsLiveData : LiveData<List<ReviewDetailsResponse>>
        get() = _reviewsLiveData

    val errorApiMessage = R.string.api_error_message
    val errorServerMessage = R.string.sever_error_message

    fun getBooks() {
        dataSource.getBooks { booksResult ->
            when(booksResult){
                is BooksResult.Success -> {
                    booksLiveData.value = booksResult.books
                    viewFlipperLiveData.value = Pair(VIEW_FLIPPER_BOOKS, null)
                }

                is BooksResult.ApiError -> {
                    if (booksResult.responseCode == 401){
                        viewFlipperLiveData.value = Pair(VIEW_FLIPPER_ERROR, R.string.books_error_401)
                    }else{
                        viewFlipperLiveData.value = Pair(VIEW_FLIPPER_ERROR, R.string.books_error_400_generic)
                    }
                }

                is BooksResult.ServerError -> {
                    viewFlipperLiveData.value = Pair(VIEW_FLIPPER_ERROR, R.string.books_error_500_generic)
                }
            }
        }
    }

    fun getReviewByAuthor(){
        dataSource.getReviewByAuthor("jeanine cummins"){ reviewResult ->
            when(reviewResult){
                is ReviewsResult.Success -> {
                    _reviewsLiveData.value = reviewResult.reviews
                }

                is ReviewsResult.ApiError -> {
                    if(reviewResult.responseCode != 200){
                        _apiErrorMessage.value = errorApiMessage
                    }
                }

                is ReviewsResult.ServerError -> {
                    _serverErrorMessage.value = errorServerMessage
                }
            }
        }
    }

     class ViewModelFactory(private val dataSource: BooksRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
                return BooksViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}