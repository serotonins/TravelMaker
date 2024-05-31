package com.ssafy.gumibom.domain.meeting.service;

import com.ssafy.gumibom.domain.meeting.dto.MeetingMemberDto;
import com.ssafy.gumibom.domain.meeting.repository.MeetingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingMemberService {
    private final MeetingMemberRepository memberRepository;

    @Transactional
    public void deleteMember(MeetingMemberDto memberDto){
        memberRepository.deleteById(memberDto.getId());
    }
}
