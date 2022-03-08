package com.egeperk.projedigieggs.viewmodel

import android.util.Log
import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.egeperk.projedigieggs.CharactersQuery
import com.egeperk.projedigieggs.adapter.ItemAdapter
import com.egeperk.projedigieggs.apolloClient
import com.egeperk.projedigieggs.repository.CharRepository
import com.egeperk.projedigieggs.view.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: CharRepository) : ViewModel() {


    val channel = Channel<Unit>(Channel.CONFLATED)
    private var lastAppliedQuery = ""
    private var page: Int? = 0
    var currentQuery = ""
    private var characters = mutableListOf<CharactersQuery.Result>()
    private val charAdapter by lazy { ItemAdapter() }


    private val _charactersList by lazy { MutableLiveData<State.ViewState<ApolloResponse<CharactersQuery.Data>>>() }

    val charactersList: LiveData<State.ViewState<ApolloResponse<CharactersQuery.Data>>>
        get() = _charactersList


    fun queryCharList() = viewModelScope.launch {
        _charactersList.postValue(State.ViewState.Loading())
        try {
            val response = repository.queryCharList(page,currentQuery)
            _charactersList.postValue(State.ViewState.Success(response))
            page = response.data?.characters?.info?.next

        } catch (e: ApolloException) {
            _charactersList.postValue(State.ViewState.Error("Error!"))
        }
    }


}