package com.example.nybooks.presentation.books

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.nybooks.R
import com.example.nybooks.data.model.Book
import com.example.nybooks.data.model.BooksResult
import com.example.nybooks.data.model.ReviewsResult
import com.example.nybooks.data.repository.BooksRepository
import com.example.nybooks.data.response.ReviewDetailsResponse
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BooksViewModelTest {

    // rule necessário para o live data pq o live data corre na main thread e para testar precisamos que corra na
    // background thread
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: BooksViewModel

    @Mock
    private lateinit var booksLiveDataObserver: Observer<List<Book>>

    @Mock
    private lateinit var viewFlipperLiveDataObserver: Observer<Pair<Int, Int?>>

    @Mock
    private lateinit var reviewsLiveDataObserver: Observer<List<ReviewDetailsResponse>>

    @Mock
    private lateinit var errorMessageObserver: Observer<Int>

    @Mock
    private lateinit var errorServerMessageObserver: Observer<Int>

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `when view model getBooks get success then sets booksLiveData`(){
        //Arrange
        val books = listOf(
            Book("title 1", "author 1", "description 1")
        )


        val resultSuccess = MockRepository(BooksResult.Success(books), null)
        viewModel = BooksViewModel(resultSuccess)
        viewModel.booksLiveData.observeForever(booksLiveDataObserver)
        viewModel.viewFlipperLiveData.observeForever(viewFlipperLiveDataObserver)

        //Act
        viewModel.getBooks()

        //Assert
        verify(booksLiveDataObserver).onChanged(books)
        verify(viewFlipperLiveDataObserver).onChanged(Pair(1, null))
    }

    @Test
    fun `when view model getBooks get api error 401 then sets viewFlipperLiveData`(){
        //Arrange
        val resultApiError = MockRepository(BooksResult.ApiError(401), null)
        viewModel = BooksViewModel(resultApiError)
        viewModel.viewFlipperLiveData.observeForever(viewFlipperLiveDataObserver)

        //Act
        viewModel.getBooks()

        //Assert
        verify(viewFlipperLiveDataObserver).onChanged(Pair(2, R.string.books_error_401))
    }

    @Test
    fun `when view model getBooks get api other code error then sets viewFlipperLiveData`(){
        //Arrange
        val resultApiError = MockRepository(BooksResult.ApiError(400), null)
        viewModel = BooksViewModel(resultApiError)
        viewModel.viewFlipperLiveData.observeForever(viewFlipperLiveDataObserver)

        //Act
        viewModel.getBooks()

        //Assert
        verify(viewFlipperLiveDataObserver).onChanged(Pair(2, R.string.books_error_400_generic))
    }

    @Test
    fun `when view model getBooks get server error then sets viewFlipperLiveData`(){
        //Arrange
        val resultServerError = MockRepository(BooksResult.ServerError, null)
        viewModel = BooksViewModel(resultServerError)
        viewModel.viewFlipperLiveData.observeForever(viewFlipperLiveDataObserver)

        //Act
        viewModel.getBooks()

        //Assert
        verify(viewFlipperLiveDataObserver).onChanged(Pair(2, R.string.books_error_500_generic))
    }


    @Test
    fun `when view model getReviewByAuthor get success then sets reviewsLiveData`(){
        //Arrange
        val reviews = listOf(
            ReviewDetailsResponse("title 1", "author 1", "description 1")
        )

        val resultSuccess = MockRepository(null, ReviewsResult.Success(reviews))
        viewModel = BooksViewModel(resultSuccess)
        viewModel.reviewsLiveData.observeForever(reviewsLiveDataObserver)

        //Act
        viewModel.getReviewByAuthor()

        //Assert
        verify(reviewsLiveDataObserver).onChanged(reviews)
    }

    @Test
    fun `when view model getReviewByAuthor get api error then sets error message`(){
        //Arrange

        val resultApiError = MockRepository(null, ReviewsResult.ApiError(400))
        viewModel = BooksViewModel(resultApiError)
        viewModel.apiErrorMessage.observeForever(errorMessageObserver)

        //Act
        viewModel.getReviewByAuthor()

        //Assert
        verify(errorMessageObserver).onChanged(viewModel.errorApiMessage)
    }


    @Test
    fun `when view model getReviewByAuthor get server error then sets error message`(){
        //Arrange

        val resultServerError = MockRepository(null, ReviewsResult.ServerError)
        viewModel = BooksViewModel(resultServerError)
        viewModel.serverErrorMessage.observeForever(errorServerMessageObserver)

        //Act
        viewModel.getReviewByAuthor()

        //Assert
        verify(errorServerMessageObserver).onChanged(viewModel.errorServerMessage)
    }

    class MockRepository(private val result: BooksResult?, private val resultReview: ReviewsResult?) : BooksRepository{
        override fun getBooks(booksResultCallback: (result: BooksResult) -> Unit) {
            booksResultCallback.invoke(result!!)
        }

        override fun getReviewByAuthor(authorName: String, reviewsResultCallback: (result: ReviewsResult) -> Unit) {
            reviewsResultCallback.invoke(resultReview!!)
        }
    }
}