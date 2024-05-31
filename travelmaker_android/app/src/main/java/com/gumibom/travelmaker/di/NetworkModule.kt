package com.gumibom.travelmaker.di


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gumibom.travelmaker.data.api.firebase.FirebaseTokenService
import com.gumibom.travelmaker.data.api.google.GoogleLocationSearchService
import com.gumibom.travelmaker.data.api.kakao.KakaoLocationSearchService
import com.gumibom.travelmaker.data.api.login.LoginService
import com.gumibom.travelmaker.data.api.meeting.MeetingService
import com.gumibom.travelmaker.data.api.meeting_post.MeetingPostService
import com.gumibom.travelmaker.data.api.myPage.MyPageService
import com.gumibom.travelmaker.data.api.naver.NaverLocationSearchService
import com.gumibom.travelmaker.data.api.pamphlet.PamphletService

import com.gumibom.travelmaker.data.api.signup.SignupService
import com.gumibom.travelmaker.data.api.token.TokenService
import com.gumibom.travelmaker.data.repository.login.LoginRepository
import com.gumibom.travelmaker.util.AccessTokenInterceptor
import com.gumibom.travelmaker.util.ApplicationClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NaverRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GoogleRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainRetrofit

    /**
     * Logging용, Interceptor용 OkHttp 구분 하기
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoggingOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AccessTokenOkHttpClient

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * TokenInterceptor 인스턴스 생성
     */
    @Provides
    @Singleton
    fun provideAccessTokenInterceptor(): AccessTokenInterceptor {
        return AccessTokenInterceptor()
    }

    // HTTP Logging용 OkHttpClient 제공
    @Provides
    @Singleton
    @LoggingOkHttpClient
    fun provideInterceptorOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()
    }

    // AccessToken 인터셉터용 OkHttpClient 제공
    @Provides
    @Singleton
    @AccessTokenOkHttpClient
    fun provideAccessTokenOkHttpClient(accessTokenInterceptor: AccessTokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(accessTokenInterceptor) // AccessToken 인터셉터 추가
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()  // JSON 파싱을 더 관대하게 처리
            .create()
    }

    @Provides
    @Singleton
    fun provideScalarsConverterFactory(): ScalarsConverterFactory {
        return ScalarsConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson : Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    @NaverRetrofit
    fun provideNaverRetrofit(
        @LoggingOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(ApplicationClass.NAVER_LOCATION_SEARCH_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @Singleton
    @MainRetrofit
    fun provideRetrofit(
        @AccessTokenOkHttpClient okHttpClient: OkHttpClient,
        scalarsConverterFactory : ScalarsConverterFactory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(ApplicationClass.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(scalarsConverterFactory)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @Singleton
    @GoogleRetrofit
    fun provideGoogleRetrofit(
        @LoggingOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(ApplicationClass.GOOGLE_GEOCODE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @Singleton
    @KakaoRetrofit
    fun provideKakaoRetrofit(
        @LoggingOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(ApplicationClass.KAKAO_LOCATION_SEARCH_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @Singleton
    fun provideFirebaseFcmTokenService(@MainRetrofit retrofit: Retrofit): FirebaseTokenService{
        return retrofit.create(FirebaseTokenService::class.java)
    }

    @Provides
    @Singleton
    fun provideNaverLocationSearchService(@NaverRetrofit retrofit: Retrofit) : NaverLocationSearchService {
        return retrofit.create(NaverLocationSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleLocationSearchService(@GoogleRetrofit retrofit: Retrofit) : GoogleLocationSearchService{
        return retrofit.create(GoogleLocationSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoService(@KakaoRetrofit retrofit : Retrofit) : KakaoLocationSearchService {
        return retrofit.create(KakaoLocationSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideSignupService(@MainRetrofit retrofit : Retrofit) : SignupService {
        return retrofit.create(SignupService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginService(@MainRetrofit retrofit : Retrofit) : LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideMeetingService(@MainRetrofit retrofit : Retrofit) : MeetingService {
        return retrofit.create(MeetingService::class.java)
    }

    @Provides
    @Singleton
    fun provideMeetingPostService(@MainRetrofit retrofit : Retrofit) : MeetingPostService {
        return retrofit.create(MeetingPostService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageService(@MainRetrofit retrofit : Retrofit) : MyPageService {
        return retrofit.create(MyPageService::class.java)
    }

    @Provides
    @Singleton
    fun providePamphletService(@MainRetrofit retrofit : Retrofit) : PamphletService {
        return retrofit.create(PamphletService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenService(@MainRetrofit retrofit : Retrofit) : TokenService {
        return retrofit.create(TokenService::class.java)
    }
}