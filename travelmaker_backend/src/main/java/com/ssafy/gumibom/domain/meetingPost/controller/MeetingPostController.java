package com.ssafy.gumibom.domain.meetingPost.controller;

import com.ssafy.gumibom.domain.meetingPost.dto.request.FindByGeoRequestDTO;
import com.ssafy.gumibom.domain.meetingPost.dto.request.RequestJoinMeetingRequestDTO;
import com.ssafy.gumibom.domain.meetingPost.dto.request.ResAboutReqJoinMeetingRequestDto;
import com.ssafy.gumibom.domain.meetingPost.dto.request.WriteMeetingPostRequestDTO;
import com.ssafy.gumibom.domain.meetingPost.dto.response.DetailOfMeetingPostResponseDTO;
import com.ssafy.gumibom.domain.meetingPost.dto.response.ShowAllJoinRequestResponseDto;
import com.ssafy.gumibom.domain.meetingPost.service.MeetingPostService;
import com.ssafy.gumibom.domain.meetingPost.service.MeetingRequestService;
import com.ssafy.gumibom.global.base.BaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Tag(name = "Meeting Post", description = "모임글 관련 api")
@RestController
@RequestMapping("/meeting-post")
@RequiredArgsConstructor
public class MeetingPostController {

    private final MeetingPostService meetingPostService;
    private final MeetingRequestService meetingRequestService;

    // 모임글 작성
    @Operation(summary = "모임글 작성")
    @ResponseBody
    @PostMapping(value = "/write", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> writeMeetingPost(
            @RequestPart(required = false) MultipartFile mainImage,
            @RequestPart(required = false) MultipartFile subImage,
            @RequestPart(required = false) MultipartFile thirdImage,
            @RequestPart WriteMeetingPostRequestDTO requestDTO) throws IOException {

        log.info("username: "+requestDTO.getUsername());
        log.info(requestDTO.getTitle());
        log.info(requestDTO.getContent());
        log.info("authdate: "+String.valueOf(requestDTO.getEndDate()));
//        meetingPostService.write(requestDTO);
//        return "redirect:/meeting-post";
        return meetingPostService.write(mainImage, subImage, thirdImage, requestDTO);
    }

    @Operation(summary = "위치 기반 모임글 리스트 필터링 조회")
    @PostMapping
    public ResponseEntity<?> inquiryMeetingPost(@RequestBody FindByGeoRequestDTO requestDTO) {

        return meetingPostService.meetingPostRadius(requestDTO.getLatitude(), requestDTO.getLongitude(), requestDTO.getRadius(), requestDTO.getCategories());
    }

    @Operation(summary = "마커 클릭 시 모임글 상세 조회")
    @GetMapping("/{meetingPostId}")
    public ResponseEntity<DetailOfMeetingPostResponseDTO> clickMarker(@PathVariable("meetingPostId") Long meetingPostId) {

        return meetingPostService.meetingPostDetail(meetingPostId);
    }

    @Operation(summary = "모임글 수정")
    @PutMapping(value = "/{meetingPostId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> modifyMeetingPost(
            @RequestPart(required = false) MultipartFile mainImage,
            @RequestPart(required = false) MultipartFile subImage,
            @RequestPart(required = false) MultipartFile thirdImage,
            @RequestBody WriteMeetingPostRequestDTO requestDTO,
            @PathVariable Long meetingPostId) throws IOException {

        return meetingPostService.modify(mainImage, subImage, thirdImage, requestDTO, meetingPostId);
    }

    @Operation(summary = "모임글 삭제")
    @DeleteMapping("/{meetingPostId}")
    public ResponseEntity<BaseResponseDto> deleteMeetingPost(@PathVariable Long meetingPostId) {

        meetingPostService.delete(meetingPostId);
        return ResponseEntity.ok(new BaseResponseDto(true, "모임글이 삭제되었습니다."));
    }

    @Operation(summary = "모임글에 참여 요청")
    @PostMapping("/request-join")
    public ResponseEntity<BaseResponseDto> requestJoinMeeting(@RequestBody RequestJoinMeetingRequestDTO rJMRDto) throws IOException {
        return meetingRequestService.requestJoin(rJMRDto);
    }

    @Operation(summary = "참여 요청 수락")
    @PostMapping("/response-join/accept")
    public ResponseEntity<BaseResponseDto> acceptRequestJoinMeeting(@RequestBody ResAboutReqJoinMeetingRequestDto rARJMRDto) throws IOException {
        return meetingRequestService.resAboutRequest(rARJMRDto, true);
    }

    @Operation(summary = "참여 요청 거절")
    @PostMapping("/response-join/refuse")
    public ResponseEntity<BaseResponseDto> refuseRequestJoinMeeting(@RequestBody ResAboutReqJoinMeetingRequestDto rARJMRDto) throws IOException {
        return meetingRequestService.resAboutRequest(rARJMRDto, false);
    }


    // 사용자 -> 모임 요청 보낸거, 받은거 조회 (추후 User Controller로 이전)
    @Operation(summary = "모임 요청 조회")
    @GetMapping("/all-request/{userId}")
    public @ResponseBody ShowAllJoinRequestResponseDto showAllJoinRequest(@PathVariable("userId") Long userId) {
        return meetingRequestService.showAllJoinRequest(userId);
    }


    @Operation(summary = "모집글 종료 api")
    @PutMapping("/post-finish/{meetingPostId}")
    public ResponseEntity<BaseResponseDto> finishMeetingPost(@PathVariable("meetingPostId") Long meetingPostId){
        meetingPostService.finishMeetingPost(meetingPostId);
        return ResponseEntity.ok(new BaseResponseDto(true, "모집글 종료"));
    }

    @Operation(summary = "모임 리스트 조회 api")
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<DetailOfMeetingPostResponseDTO>> getMyMeeting(@PathVariable Long userId){
        List<DetailOfMeetingPostResponseDTO> meetingPostResponseDTOs = meetingPostService.getMeetingPostByUserId(userId);
        return ResponseEntity.ok(meetingPostResponseDTOs);
    }

    @Operation(summary = "모임 종료 api")
    @PutMapping("/meeting-finish/{meetingPostId}")
    public ResponseEntity<BaseResponseDto> finishMeeting(@PathVariable("meetingPostId") Long meetingPostId){
        meetingPostService.finishMeeting(meetingPostId);
        return ResponseEntity.ok(new BaseResponseDto(true, "모임 종료"));
    }

    @Operation(summary = "모임 신청 취소 api")
    @DeleteMapping("/delete/{userId}/{meetingPostId}")
    public ResponseEntity<BaseResponseDto> leaveMeeting(@PathVariable("userId") Long userId, @PathVariable("meetingPostId") Long meetingPostId){
        meetingPostService.leaveMeeting(userId, meetingPostId);
        return ResponseEntity.ok(new BaseResponseDto(true, "모임 신청이 취소되었습니다."));
    }
}
