package com.codingwithmitch.mviexample.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codingwithmitch.mviexample.DateStateListener
import com.codingwithmitch.mviexample.R
import com.codingwithmitch.mviexample.model.BlogPost
import com.codingwithmitch.mviexample.model.User
import com.codingwithmitch.mviexample.ui.main.adapter.MainRecyclerAdapter
import com.codingwithmitch.mviexample.ui.main.state.MainStateEvent
import com.codingwithmitch.mviexample.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ClassCastException
import java.lang.Exception

class MainFragment : Fragment(), MainRecyclerAdapter.Interaction {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var dateStateListener: DateStateListener
    private lateinit var mainRecyclerAdapter: MainRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        mainViewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        subscribeObservers()
        initRecyclerView()
    }

    private fun subscribeObservers(){
        mainViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            //Handle loading and message
            dateStateListener.onDataStateChange(dataState)

            //Handle Data
            dataState.data?.let { mainViewState ->

                mainViewState.getContentIfNotHandled()?.let {
                    it.blogPosts?.let { blogPosts ->
                        // set blog posts data
                        mainViewModel.setBlogListData(blogPosts)
                    }

                    it.user?.let { user ->
                        //set user data
                        mainViewModel.setUser(user)
                    }
                }
            }

            //Handle Error
            dataState.message?.let {

            }

            //Handle Loading
            dataState.loading.let {

            }
        })

        mainViewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            viewState.blogPosts?.let { blogsList->
                mainRecyclerAdapter.submitList(blogsList)
            }

            viewState.user?.let { user ->
                setUserProperties(user)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blogs -> triggerGetBlogsEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            mainRecyclerAdapter = MainRecyclerAdapter(this@MainFragment)
            adapter = mainRecyclerAdapter
        }
    }

    private fun setUserProperties(user: User){
        email.text = user.email
        username.text = user.username

        view?.let {
            Glide.with(it.context).load(user.image).into(image)
        }
    }

    private fun triggerGetBlogsEvent() {
        mainViewModel.setEventState(MainStateEvent.GetBlogPostsEvent())
    }

    private fun triggerGetUserEvent() {
        mainViewModel.setEventState(MainStateEvent.GetUserEvent("1"))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dateStateListener = context as DateStateListener
        }catch (e : ClassCastException){
            e.stackTrace
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        //handle clicks on recycler view
    }
}