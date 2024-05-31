package com.ssafy.gumibom.domain.meeting.controller;

import com.ssafy.gumibom.domain.meeting.dto.MeetingCreateResponseDto;
import com.ssafy.gumibom.domain.meeting.dto.MeetingFinishResponseDto;
import com.ssafy.gumibom.domain.meeting.dto.MeetingDto;
import com.ssafy.gumibom.domain.meeting.service.MeetingService;
import com.ssafy.gumibom.domain.meetingPost.dto.DetailMeetingPostResForMeetingDto;
import com.ssafy.gumibom.domain.meetingPost.service.MeetingPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Meeting", description = "모임 관련 api")
@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final MeetingPostService meetingPostService;

    // 모임 생성
//    @Operation(summary = "모임 생성 api")
//    @GetMapping("/new/{meetingPostId}")
//    public ResponseEntity<?> createMeeting(@PathVariable Long meetingPostId) {
//        try {
//            DetailMeetingPostResForMeetingDto detailMeetingPostResForMeetingDto = meetingPostService.meetingPostDetailRead(meetingPostId);
//            meetingPostService.finishMeetingPost(meetingPostId);
//            meetingService.createMeeting(detailMeetingPostResForMeetingDto);
//            MeetingCreateResponseDto meetingCreateResponseDto = new MeetingCreateResponseDto(true,"모임이 성공적으로 시작됩니다.");
//            return ResponseEntity.ok(meetingCreateResponseDto);
//        } catch (Exception e) {
//            // 로깅, 오류 처리, 사용자 정의 예외에 따라 다름
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("모임 생성 중 오류가 발생했습니다: " + e.getMessage());
//        }
//    }
    // 내 모임 리스트 조회
//    @Operation(summary = "모임 리스트 조회 api")
//    @GetMapping("/list/{userId}")
//    public ResponseEntity<List<MeetingDto>> getMeetingsByUserId(@PathVariable Long userId) {
//        List<MeetingDto> meetings = meetingService.getMeetingsByUserId(userId);
//        return ResponseEntity.ok(meetings);
//    }


    // 모임 종료
//    @Operation(summary = "모임 종료 api")
//    @PutMapping("/{meetingId}")
//    public ResponseEntity<MeetingFinishResponseDto> finishMeeting(@PathVariable("meetingId") Long meetingId){
//        Boolean status = meetingService.finishMeeting(meetingId);
//        MeetingFinishResponseDto meetingFinishResponseDto = new MeetingFinishResponseDto(status);
//        return ResponseEntity.ok(meetingFinishResponseDto);
//    }

//    @GetMapping("/{meetingPostId}/{meetingId}")
//    public ResponseEntity<MeetingDetailResDto> meetingDetail(MeetingDetailReqDto meetingDetailReqDto) {
//        MeetingDetailResDto meetingDetailResDto = meetingService.getMeetingByMeetingId(meetingDetailReqDto);
//        return ResponseEntity.ok(meetingDetailResDto);
//    }

}
