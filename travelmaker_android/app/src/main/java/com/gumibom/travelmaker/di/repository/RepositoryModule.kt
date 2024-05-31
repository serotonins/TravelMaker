package com.gumibom.travelmaker.di.repository


import com.gumibom.travelmaker.data.api.signup.SignupService
import com.gumibom.travelmaker.data.datasource.firebase.FirebaseFcmRemoteDataSource
import com.gumibom.travelmaker.data.datasource.google.GoogleLocationRemoteDataSource
import com.gumibom.travelmaker.data.datasource.kakao.KakaoLocationRemoteDataSource
import com.gumibom.travelmaker.data.datasource.login.LoginRemoteDataSource
import com.gumibom.travelmaker.data.datasource.meeting.MeetingRemoteDataSource
import com.gumibom.travelmaker.data.datasource.meeting_post.MeetingPostRemoteDataSource
import com.gumibom.travelmaker.data.datasource.myPage.MyPageRemoteDataSource
import com.gumibom.travelmaker.data.datasource.naver.NaverLocationRemoteDataSource
import com.gumibom.travelmaker.data.datasource.pamphlet.PamphletRemoteDataSource
import com.gumibom.travelmaker.data.datasource.signup.SignupRemoteDataSource
import com.gumibom.travelmaker.data.datasource.signup.SignupRemoteDataSourceImpl
import com.gumibom.travelmaker.data.repository.firebase.FirebaseFcmRepository
import com.gumibom.travelmaker.data.repository.firebase.FirebaseFcmRepositoryImpl

import com.gumibom.travelmaker.data.repository.google.GoogleLocationRepository
import com.gumibom.travelmaker.data.repository.google.GoogleLocationRepositoryImpl
import com.gumibom.travelmaker.data.repository.kakao.KakaoLocationRepository
import com.gumibom.travelmaker.data.repository.kakao.KakaoLocationRepositoryImpl
import com.gumibom.travelmaker.data.repository.login.LoginRepository
import com.gumibom.travelmaker.data.repository.login.LoginRepositoryImpl
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepository
import com.gumibom.travelmaker.data.repository.meeting.MeetingRepositoryImpl
import com.gumibom.travelmaker.data.repository.meeting_post.MeetingPostRepository
import com.gumibom.travelmaker.data.repository.meeting_post.MeetingPostRepositoryImpl
import com.gumibom.travelmaker.data.repository.myPage.MyPageRepository
import com.gumibom.travelmaker.data.repository.myPage.MyPageRepositoryImpl
import com.gumibom.travelmaker.data.repository.naver.NaverLocationRepository
import com.gumibom.travelmaker.data.repository.naver.NaverLocationRepositoryImpl
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepository
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepositoryImpl

import com.gumibom.travelmaker.data.repository.signup.SignupRepository
import com.gumibom.travelmaker.data.repository.signup.SignupRepositoryImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    //파이어베이스 관련 Repo
    @Singleton
    @Provides
    fun provideFirebaseFcmRepository(firebaseFcmRemoteDataSource: FirebaseFcmRemoteDataSource):FirebaseFcmRepository{
        return FirebaseFcmRepositoryImpl(firebaseFcmRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideNaverLocationRepository(naverLocationRemoteDataSource: NaverLocationRemoteDataSource) : NaverLocationRepository {
        return NaverLocationRepositoryImpl(naverLocationRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideGoogleLocationRepository(googleLocationRemoteDataSource: GoogleLocationRemoteDataSource) : GoogleLocationRepository {
        return GoogleLocationRepositoryImpl(googleLocationRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideKakaoLocationRepository(kakaoLocationRemoteDataSource: KakaoLocationRemoteDataSource) : KakaoLocationRepository {
        return KakaoLocationRepositoryImpl(kakaoLocationRemoteDataSource)
    }

    // 회원가입 레퍼지토리
    @Singleton
    @Provides
    fun provideSignupRepository(signupRemoteDataSource : SignupRemoteDataSource) : SignupRepository {
        return SignupRepositoryImpl(signupRemoteDataSource)
    }

    // 로그인 레퍼지토리
    @Singleton
    @Provides
    fun provideLoginRepository(loginRemoteDataSource : LoginRemoteDataSource) : LoginRepository {
        return LoginRepositoryImpl(loginRemoteDataSource)
    }

    // 모임 관련 레퍼지토리
    @Singleton
    @Provides
    fun provideMeetingRepository(meetingRemoteDataSource : MeetingRemoteDataSource) : MeetingRepository {
        return MeetingRepositoryImpl(meetingRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideMeetingPostRepository(meetingPostRemoteDataSource : MeetingPostRemoteDataSource) : MeetingPostRepository {
        return MeetingPostRepositoryImpl(meetingPostRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideMyPageRepository(myPageRemoteDataSource: MyPageRemoteDataSource) : MyPageRepository {
        return MyPageRepositoryImpl(myPageRemoteDataSource)
    }

    @Singleton
    @Provides
    fun providePamphletRepository(pamphletRemoteDataSource: PamphletRemoteDataSource) : PamphletRepository {
        return PamphletRepositoryImpl(pamphletRemoteDataSource)
    }
}