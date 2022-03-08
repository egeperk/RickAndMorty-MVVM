package com.egeperk.projedigieggs.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.egeperk.projedigieggs.CharactersQuery
import kotlinx.coroutines.flow.Flow

interface CharRepository {

    suspend fun queryCharList(page: Int?, query : String?): ApolloResponse<CharactersQuery.Data>

}