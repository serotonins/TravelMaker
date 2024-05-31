package com.gumibom.travelmaker.di.usecase.meeting

import com.gumibom.travelmaker.data.repository.meeting.MeetingRepository
import com.gumibom.travelmaker.domain.meeting.GetMarkerPositionsUseCase
import com.gumibom.travelmaker.domain.meeting.GetMyMeetingGroupListUseCase
import com.gumibom.travelmaker.domain.meeting.PutActiveChattingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MeetingUseCaseModule {

    @Singleton
    @Provides
    fun provideGetMarkerPositionsUseCase(meetingRepository: MeetingRepository) : GetMarkerPositionsUseCase {
        return GetMarkerPositionsUseCase(meetingRepository)
    }

    //그룹 리스트 받는 싱글톤 힐트 추가.
    @Singleton
    @Provides
    fun provideGetMyMeetingGroupUseCase(meetingRepository: MeetingRepository): GetMyMeetingGroupListUseCase{
        return GetMyMeetingGroupListUseCase(meetingRepository)
    }

    @Singleton
    @Provides
    fun providePutActiveChatting(meetingRepository: MeetingRepository): PutActiveChattingUseCase{
        return PutActiveChattingUseCase(meetingRepository)
    }

}