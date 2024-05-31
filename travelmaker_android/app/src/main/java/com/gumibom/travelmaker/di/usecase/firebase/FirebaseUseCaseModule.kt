package com.gumibom.travelmaker.di.usecase.firebase

import com.gumibom.travelmaker.data.repository.firebase.FirebaseFcmRepository
import com.gumibom.travelmaker.data.repository.google.GoogleLocationRepository
import com.gumibom.travelmaker.domain.firebase.FirebaseAcceptCrewUseCase
import com.gumibom.travelmaker.domain.firebase.FirebaseFcmUploadTokenUseCase
import com.gumibom.travelmaker.domain.firebase.FirebaseNotifyListUseCase
import com.gumibom.travelmaker.domain.firebase.FirebaseRefuseCrewUseCase
import com.gumibom.travelmaker.domain.firebase.FirebaseRequestGroupUseCase
import com.gumibom.travelmaker.domain.signup.GetGoogleLocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseUseCaseModule {

    @Singleton
    @Provides
    fun provideFirebaseRefuseCrewUseCase(firebaseFcmRepository: FirebaseFcmRepository):FirebaseRefuseCrewUseCase{
        return FirebaseRefuseCrewUseCase(firebaseFcmRepository)
    }

    @Singleton
    @Provides
    fun provideFirebaseAcceptCrewUseCase(firebaseFcmRepository: FirebaseFcmRepository):FirebaseAcceptCrewUseCase{
        return FirebaseAcceptCrewUseCase(firebaseFcmRepository)
    }
    @Singleton
    @Provides
    fun provideFirebaseFcmTokenUseCase(firebaseFcmRepository: FirebaseFcmRepository) : FirebaseFcmUploadTokenUseCase {
        return FirebaseFcmUploadTokenUseCase(firebaseFcmRepository)
    }

    @Singleton
    @Provides
    fun provideFcmGroupRequestUseCase(firebaseFcmRepository: FirebaseFcmRepository) : FirebaseRequestGroupUseCase{
        return FirebaseRequestGroupUseCase(firebaseFcmRepository)
    }


    @Singleton
    @Provides
    fun provideFcmGetNotifyList(firebaseFcmRepository: FirebaseFcmRepository): FirebaseNotifyListUseCase{


        return FirebaseNotifyListUseCase(firebaseFcmRepository)

    }


}