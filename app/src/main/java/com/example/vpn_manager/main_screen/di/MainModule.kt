package com.example.vpn_manager.main_screen.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.vpn_manager.main_screen.data.LoginRepositoryImpl
import com.example.vpn_manager.main_screen.domain.repository.MainRepository
import com.example.vpn_manager.main_screen.presentation.viewmodel.MainViewModel
import com.example.vpn_manager.storage.datasource.StorageFileDataSource
import com.example.vpn_manager.storage.repository.StorageRepository
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class MainModule{


    @Binds
    @Singleton
    abstract fun provideLoginRepository(impl: LoginRepositoryImpl) : MainRepository
}