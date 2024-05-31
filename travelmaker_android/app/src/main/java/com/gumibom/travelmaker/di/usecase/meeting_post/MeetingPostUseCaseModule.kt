package com.gumibom.travelmaker.di.usecase.meeting_post

import com.gumibom.travelmaker.data.repository.meeting.MeetingRepository
import com.gumibom.travelmaker.data.repository.meeting_post.MeetingPostRepository
import com.gumibom.travelmaker.domain.meeting.GetMarkerPositionsUseCase
import com.gumibom.travelmaker.domain.meeting_post.PostMeetingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MeetingPostUseCaseModule {

    @Singleton
    @Provides
    fun providePostMeetingUseCase(meetingPostRepository: MeetingPostRepository) : PostMeetingUseCase {
        return PostMeetingUseCase(meetingPostRepository)
    }
}