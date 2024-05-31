package com.gumibom.travelmaker.di.usecase.login

import com.gumibom.travelmaker.data.repository.firebase.FirebaseFcmRepository
import com.gumibom.travelmaker.data.repository.login.LoginRepository
import com.gumibom.travelmaker.domain.firebase.FirebaseFcmUploadTokenUseCase
import com.gumibom.travelmaker.domain.login.FindIdUseCase
import com.gumibom.travelmaker.domain.login.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoginUseCaseModule {

    @Singleton
    @Provides
    fun provideLoginUseCase(loginRepository: LoginRepository) : LoginUseCase {
        return LoginUseCase(loginRepository)
    }

    @Singleton
    @Provides
    fun provideFindIdUseCase(loginRepository: LoginRepository) : FindIdUseCase {
        return FindIdUseCase(loginRepository)
    }

}