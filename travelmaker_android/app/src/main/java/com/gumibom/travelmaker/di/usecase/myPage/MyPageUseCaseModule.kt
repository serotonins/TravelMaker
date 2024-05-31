package com.gumibom.travelmaker.di.usecase.myPage

import com.gumibom.travelmaker.data.repository.myPage.MyPageRepository
import com.gumibom.travelmaker.domain.mypage.DeleteUserUseCase
import com.gumibom.travelmaker.domain.mypage.GetAllUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MyPageUseCaseModule {
    @Singleton
    @Provides
    fun provideGetAllUserUseCase(myPageRepository: MyPageRepository) : GetAllUserUseCase {
        return GetAllUserUseCase(myPageRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteUserUseCase(myPageRepository: MyPageRepository) : DeleteUserUseCase {
        return DeleteUserUseCase(myPageRepository)
    }
}