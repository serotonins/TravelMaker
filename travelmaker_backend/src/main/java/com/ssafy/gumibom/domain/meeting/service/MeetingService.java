package com.ssafy.gumibom.domain.meeting.service;


import com.ssafy.gumibom.domain.meeting.dto.MeetingDto;
import com.ssafy.gumibom.domain.meeting.dto.MeetingMemberDto;
import com.ssafy.gumibom.domain.meeting.entity.Meeting;
import com.ssafy.gumibom.domain.meeting.entity.MeetingMember;
import com.ssafy.gumibom.domain.meeting.repository.MeetingMemberRepository;
import com.ssafy.gumibom.domain.meeting.repository.MeetingRepository;
import com.ssafy.gumibom.domain.meeting.repository.MeetingRepositoryQuery;
import com.ssafy.gumibom.domain.meetingPost.dto.DetailMeetingPostResForMeetingDto;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingApplier;
import com.ssafy.gumibom.domain.user.dto.UserDto;
import com.ssafy.gumibom.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 전역으로 readOnly = true 선언,데이터 쓰기 시에만 @Transactional 추가
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingRepositoryQuery meetingRepositoryQuery;

    // 사용자의 아이디를 통해 사용자가 진행했던 모임 조회
    public List<MeetingDto> getMeetingsByUserId(Long userId) {
        // 사용자 ID로 Meeting 엔티티 검색
        List<Meeting> meetings = meetingRepositoryQuery.findByUserId(userId);

        // Meeting 엔티티를 MeetingResDto로 변환
        return meetings.stream()
                .map(this::convertToMeetingResDto)
                .collect(Collectors.toList());
    }

    // Meeting 엔티티를 MeetingResDto로 변환
    private MeetingDto convertToMeetingResDto(Meeting meeting) {
        List<MeetingMemberDto> memberDtos = meeting.getMeetingMembers().stream()
                .map(this::convertToMeetingMemberDto)
                .collect(Collectors.toList());

        return MeetingDto.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .imgUrl(meeting.getImgUrl())
                .isFinish(meeting.getIsFinish())
                .members(memberDtos)
                .build();
    }

    private MeetingMemberDto convertToMeetingMemberDto(MeetingMember meetingMember) {
        return MeetingMemberDto.builder()
                .id(meetingMember.getId())
                .isNative(meetingMember.getIsNative())
                .isHead(meetingMember.getIsHead())
                .build();
    }


    // 여행객 최소인원과 신청한 여행객 인원을 비교하고 현지인 최소인원과 신청한 현지인 인원을 비교하여
    // 최소인원 충족 시 모임을 생성하는 메서드
    // 미충족시 예외처리
    @Transactional
    public void createMeeting(DetailMeetingPostResForMeetingDto detailMeetingPostResForMeetingDto) {
//        Meeting 엔티티 생성
//        if (detailMeetingPostResForMeetingDto.getNativeMin() <= meetingMemberRepositoryQuery.countByIsNativeTrue(detailMeetingPostResForMeetingDto.getId()) &&
//                detailMeetingPostResForMeetingDto.getTravelerMin() <= meetingMemberRepositoryQuery.countByIsNativeFalse(detailMeetingPostResForMeetingDto.getId())) {
//        } else {
//            // 조건 불만족 시 예외 발생
//            throw new MeetingCreationException("최소인원을 채우지 못해서 모임을 시작할 수 없습니다.");
//        }
        Meeting meeting = new Meeting(detailMeetingPostResForMeetingDto);
        meetingRepository.save(meeting);

        // MeetingApplier 리스트를 MeetingMember 리스트로 변환
        List<MeetingMember> members = convertAppliersToMembers(detailMeetingPostResForMeetingDto.getMembers(), meeting);

        // 각 MeetingMember를 저장
        members.forEach(member -> {
            meetingMemberRepository.save(member);
        });
    }


    // 신청자 데이터를 멤버 데이터로 변환하는 메서드를 stream.map을 통해 신청자 리스트의 데이터 하나하나를 멤버 데이터로 전환하고
    // 멤버리스트를 반환하는 메서드
    private List<MeetingMember> convertAppliersToMembers(List<MeetingApplier> appliers, Meeting meeting) {
        return appliers.stream()
                .map(applier -> createMeetingMemberFromApplier(applier, meeting))
                .collect(Collectors.toList());
    }

    // 신청자 데이터를 멤버 데이터로 변환하는 메서드
    private MeetingMember createMeetingMemberFromApplier(MeetingApplier applier, Meeting meeting) {
        MeetingMember member = new MeetingMember();
        member.setUser(applier.getUser());
        member.setMeeting(meeting);
        meeting.getMeetingMembers().add(member);
        // 여기에서 MeetingMember의 다른 필드를 설정할 수 있습니다.
        member.setIsNative(applier.getIsNative());
        member.setIsHead(applier.getIsHead());
        return member;
    }

    // 모임이 끝날 때 방장이 모임 종료 버튼을 클릭하면
    // 해당 메서드가 진행되어 meeting의 isFinish가 true로 바뀐다
    @Transactional
    public Boolean finishMeeting(Long meetingId) {
        Meeting meeting = meetingRepositoryQuery.findByMeetingId(meetingId);
        meeting.finishMeeting();
        return meeting.getIsFinish();
    }

}
