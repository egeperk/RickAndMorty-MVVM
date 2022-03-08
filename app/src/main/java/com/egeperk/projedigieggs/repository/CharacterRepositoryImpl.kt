package com.egeperk.projedigieggs.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.egeperk.projedigieggs.BaseApi
import com.egeperk.projedigieggs.CharactersQuery
import com.egeperk.projedigieggs.adapter.ItemAdapter
import com.egeperk.projedigieggs.apolloClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val service: BaseApi) : CharRepository {


    override suspend fun queryCharList(
        page: Int?,
        query: String?
    ): ApolloResponse<CharactersQuery.Data> {
        return service.getApolloClient().query(CharactersQuery(Optional.Present(page))).execute()
    }
}



