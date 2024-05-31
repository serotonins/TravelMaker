package com.gumibom.travelmaker.di.usecase.pamphlet

import com.gumibom.travelmaker.data.repository.myPage.MyPageRepository
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepository
import com.gumibom.travelmaker.domain.mypage.GetAllUserUseCase
import com.gumibom.travelmaker.domain.pamphlet.DeleteRecordUseCase
import com.gumibom.travelmaker.domain.pamphlet.FinishTravelPamphletUseCase
import com.gumibom.travelmaker.domain.pamphlet.GetAllMyRecordUseCase
import com.gumibom.travelmaker.domain.pamphlet.GetMyRecordUseCase
import com.gumibom.travelmaker.domain.pamphlet.GetOtherPamphletUseCase
import com.gumibom.travelmaker.domain.pamphlet.MakePamphletUseCase
import com.gumibom.travelmaker.domain.pamphlet.MakeRecordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PamphletUseCaseModule {

    @Singleton
    @Provides
    fun provideMakePamphletUseCase(pamphletRepository: PamphletRepository) : MakePamphletUseCase {
        return MakePamphletUseCase(pamphletRepository)
    }

    @Singleton
    @Provides
    fun provideGetMyRecordUseCase(pamphletRepository: PamphletRepository) : GetMyRecordUseCase {
        return GetMyRecordUseCase(pamphletRepository)
    }

    @Singleton
    @Provides
    fun provideFinishMyTravelPamphletUseCase(pamphletRepository: PamphletRepository) : FinishTravelPamphletUseCase {
        return FinishTravelPamphletUseCase(pamphletRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllMyRecordUseCase(pamphletRepository: PamphletRepository) : GetAllMyRecordUseCase {
        return GetAllMyRecordUseCase(pamphletRepository)
    }

    @Singleton
    @Provides
    fun provideMakeRecordUseCase(pamphletRepository: PamphletRepository) : MakeRecordUseCase {
        return MakeRecordUseCase(pamphletRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteRecordUseCase(pamphletRepository: PamphletRepository) : DeleteRecordUseCase {
        return DeleteRecordUseCase(pamphletRepository)
    }

    @Singleton
    @Provides
    fun provideGetOtherPamphletUseCase(pamphletRepository: PamphletRepository) : GetOtherPamphletUseCase {
        return GetOtherPamphletUseCase(pamphletRepository)
    }
}