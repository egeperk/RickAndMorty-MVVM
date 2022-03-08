package com.egeperk.projedigieggs.di

import com.egeperk.projedigieggs.repository.CharRepository
import com.egeperk.projedigieggs.repository.CharacterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepo(repo: CharacterRepositoryImpl): CharRepository

}