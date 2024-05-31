package com.ssafy.gumibom.domain.meetingPost.service;

import com.ssafy.gumibom.domain.meetingPost.dto.request.RequestJoinMeetingRequestDTO;
import com.ssafy.gumibom.domain.meetingPost.dto.request.ResAboutReqJoinMeetingRequestDto;
import com.ssafy.gumibom.domain.meetingPost.dto.response.ReceivedRequestResponseDto;
import com.ssafy.gumibom.domain.meetingPost.dto.response.SentRequestResponseDto;
import com.ssafy.gumibom.domain.meetingPost.dto.response.ShowAllJoinRequestResponseDto;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingApplier;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingPost;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingRequest;
import com.ssafy.gumibom.domain.meetingPost.repository.MeetingApplierRepository;
import com.ssafy.gumibom.domain.meetingPost.repository.MeetingPostRepository;
import com.ssafy.gumibom.domain.meetingPost.repository.MeetingRequestRepository;
import com.ssafy.gumibom.domain.user.entity.User;
import com.ssafy.gumibom.domain.user.repository.UserRepository;
import com.ssafy.gumibom.global.base.BaseResponseDto;
import com.ssafy.gumibom.global.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingRequestService {

    private final UserRepository userRepository;
    private final MeetingPostRepository meetingPostRepository;
    private final MeetingRequestRepository meetingRequestRepository;
    private final MeetingApplierRepository meetingApplierRepository;

    private final FcmService fcmService;


    @Transactional
    public ResponseEntity<BaseResponseDto> requestJoin(RequestJoinMeetingRequestDTO rJMRDto) throws IOException {

        // 이미 해당 모임 게시글에 요청을 한 내역이 있는 경우
        if(meetingRequestRepository.existRequest(rJMRDto.getRequestorId(), rJMRDto.getMeetingPostId())) {
//            return ResponseEntity.ok("해당 모임 게시글에 이미 요청한 적이 있습니다.");
            return ResponseEntity.ok(new BaseResponseDto(false, "해당 모임 게시글에 이미 요청한 적이 있습니다."));
        }

        User requestor = userRepository.findOne(rJMRDto.getRequestorId());
        MeetingPost meetingPost = meetingPostRepository.findOne(rJMRDto.getMeetingPostId());
        User acceptor = meetingPost.getHead();

        MeetingRequest meetingRequest = MeetingRequest.createRequest(meetingPost, requestor, acceptor);

        fcmService.sendMessageTo(acceptor.getFcmtoken(), "모임 요청", requestor.getUsername()+"님이 모임 요청을 보냈습니다.");

        meetingRequestRepository.save(meetingRequest);

        return ResponseEntity.ok(new BaseResponseDto(true, "모임 요청이 완료되었습니다."));
    }

    public ShowAllJoinRequestResponseDto showAllJoinRequest(Long userId) {

        List<MeetingRequest> sentRequests = meetingRequestRepository.findSentByUserId(userId);
        List<MeetingRequest> receivedRequests = meetingRequestRepository.findReceivedByUserId(userId);

        List<ReceivedRequestResponseDto> receivedRequestsDto = receivedRequests.stream()
                .map(req -> new ReceivedRequestResponseDto(req))
                .collect(Collectors.toList());

        List<SentRequestResponseDto> sentRequestsDto = sentRequests.stream()
                .map(req -> new SentRequestResponseDto(req))
                .collect(Collectors.toList());

        return new ShowAllJoinRequestResponseDto(receivedRequestsDto, sentRequestsDto);
    }

    @Transactional
    public ResponseEntity<BaseResponseDto> resAboutRequest(ResAboutReqJoinMeetingRequestDto rARJMRDto, Boolean isAccept) throws IOException {
        User requestor = userRepository.findOne(rARJMRDto.getRequestorId());
        MeetingPost meetingPost = meetingPostRepository.findOne(rARJMRDto.getMeetingPostId());
        MeetingRequest request = meetingRequestRepository.findOne(rARJMRDto.getRequestId());

        // 이미 응답을 받은 모임 요청이라면 -> 바로 리턴
        if(request.getWhetherGetResponse()) {
            return ResponseEntity.ok(new BaseResponseDto(false, "이미 승낙/거절 응답을 받은 모임 요청입니다. "));
        }

        if(isAccept) {
            MeetingApplier applier = meetingPost.addApplier(requestor, false); // 게시글에 meetingApplier로 추가
            meetingApplierRepository.save(applier);
        }

        request.getResponse();
        meetingRequestRepository.delete(request);

        String messageTitle = (isAccept) ? "모임 승낙" : "모임 거절";
        String messageBody = meetingPost.getTitle();
        messageBody += (isAccept) ? " 모임 참여자가 되었습니다." : "모임 요청을 거절당했습니다.";
        String responseMessage = (isAccept) ? "모임 요청을 승낙했습니다." : "모임 요청을 거절했습니다.";

        fcmService.sendMessageTo(requestor.getFcmtoken(), messageTitle, messageBody); // FCM 토큰으로 노티 전달

        return ResponseEntity.ok(new BaseResponseDto(true, responseMessage));
    }

}
