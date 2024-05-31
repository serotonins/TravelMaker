package com.gumibom.travelmaker.di.usecase.signup

import com.gumibom.travelmaker.data.repository.google.GoogleLocationRepository
import com.gumibom.travelmaker.data.repository.naver.NaverLocationRepository

import com.gumibom.travelmaker.data.repository.signup.SignupRepository
import com.gumibom.travelmaker.domain.signup.CheckCertificationUseCase
import com.gumibom.travelmaker.domain.signup.CheckDuplicatedIdUseCase
import com.gumibom.travelmaker.domain.signup.CheckDuplicatedNicknameUseCase
import com.gumibom.travelmaker.domain.signup.CheckSecretNumberUseCase
import com.gumibom.travelmaker.domain.signup.GetGoogleLocationUseCase
import com.gumibom.travelmaker.domain.signup.SaveUserInfoUseCase
import com.gumibom.travelmaker.domain.signup.SendPhoneNumberUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SignupUseCaseModule {

    @Singleton
    @Provides
    fun provideGetGoogleLocationUseCase(googleLocationRepository: GoogleLocationRepository) : GetGoogleLocationUseCase {
        return GetGoogleLocationUseCase(googleLocationRepository)
    }

    @Singleton
    @Provides
    fun provideSendPhoneNumberUseCase(signupRepository : SignupRepository) : SendPhoneNumberUseCase {
        return SendPhoneNumberUseCase(signupRepository)
    }

    @Singleton
    @Provides
    fun provideCheckSecretNumberUseCase(signupRepository : SignupRepository) : CheckSecretNumberUseCase {
        return CheckSecretNumberUseCase(signupRepository)
    }

    @Singleton
    @Provides
    fun provideCheckDuplicatedIdUseCase(signupRepository: SignupRepository) : CheckDuplicatedIdUseCase {
        return CheckDuplicatedIdUseCase(signupRepository)
    }

    @Singleton
    @Provides
    fun provideCheckDuplicatedNicknameUseCase(signupRepository: SignupRepository) : CheckDuplicatedNicknameUseCase {
        return CheckDuplicatedNicknameUseCase(signupRepository)
    }

    @Singleton
    @Provides
    fun provideCheckCertificationUseCase(signupRepository: SignupRepository) : CheckCertificationUseCase {
        return CheckCertificationUseCase(signupRepository)
    }

    @Singleton
    @Provides
    fun provideSaveUserDataUseCase(signupRepository: SignupRepository):SaveUserInfoUseCase{
        return SaveUserInfoUseCase(signupRepository)
    }
}