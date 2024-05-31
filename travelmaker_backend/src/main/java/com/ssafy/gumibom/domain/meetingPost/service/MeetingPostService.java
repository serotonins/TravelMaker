package com.ssafy.gumibom.domain.meetingPost.service;

import com.ssafy.gumibom.domain.meetingPost.dto.DetailMeetingPostResForMeetingDto;
import com.ssafy.gumibom.domain.meetingPost.dto.response.DetailOfMeetingPostResponseDTO;
import com.ssafy.gumibom.domain.meetingPost.dto.request.WriteMeetingPostRequestDTO;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingApplier;
import com.ssafy.gumibom.domain.meetingPost.entity.MeetingPost;
import com.ssafy.gumibom.domain.meetingPost.repository.MeetingApplierRepository;
import com.ssafy.gumibom.domain.meetingPost.repository.MeetingPostRepository;
import com.ssafy.gumibom.domain.user.entity.User;
import com.ssafy.gumibom.domain.user.repository.UserRepository;
import com.ssafy.gumibom.global.base.BaseResponseDto;
import com.ssafy.gumibom.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingPostService {

    private final UserRepository userRepository;
    private final MeetingPostRepository meetingPostRepository;
    private final MeetingApplierRepository meetingApplierRepository;

    private final S3Service s3Service;

    // 모임글 생성

    @Transactional
    public ResponseEntity<BaseResponseDto> write(
            MultipartFile mainImage,
            MultipartFile subImage,
            MultipartFile thirdImage,
            WriteMeetingPostRequestDTO writeMeetingPostRequestDTO) throws IOException {

        User author = userRepository.findByUsername(writeMeetingPostRequestDTO.getUsername());

        String mainImgUrl = "";
        String subImgUrl = "";
        String thirdImgUrl = "";

        if (mainImage != null) mainImgUrl = s3Service.uploadS3(mainImage, "images");
        if (mainImage != null) subImgUrl = s3Service.uploadS3(subImage, "images");
        if (mainImage != null) thirdImgUrl = s3Service.uploadS3(thirdImage, "images");

        MeetingPost meetingPost = MeetingPost.createMeetingPost(mainImgUrl, subImgUrl, thirdImgUrl, writeMeetingPostRequestDTO, author);
        log.info("게시글 제목: " + meetingPost.getTitle());
        log.info("데드라인: " + meetingPost.getDeadline());
        log.info("마감 날짜: " + meetingPost.getEndDate());

        meetingApplierRepository.save(meetingPost.getAppliers().get(0)); // 모임장 db에 저장
        meetingPostRepository.save(meetingPost);

        return ResponseEntity.ok(new BaseResponseDto(true, "모임글 작성 성공"));
    }


    // 마커 클릭
    @Transactional
    public ResponseEntity<DetailOfMeetingPostResponseDTO> meetingPostDetail(Long meetingPostId) {

        MeetingPost meetingPost = meetingPostRepository.findOne(meetingPostId);
        User head = meetingPost.getHead();

        DetailOfMeetingPostResponseDTO responseDTO = new DetailOfMeetingPostResponseDTO(head, meetingPost);

        return ResponseEntity.ok(responseDTO);
    }

    @Transactional
    public Boolean finishMeetingPost(Long meetingPostId) {
        MeetingPost meetingPost = meetingPostRepository.findOne(meetingPostId);
        meetingPost.updateMeetingPostStatus();
        return meetingPost.getIsFinish();
    }

    @Transactional
    public Boolean finishMeeting(Long meetingPostId) {
        MeetingPost meetingPost = meetingPostRepository.findOne(meetingPostId);
        meetingPost.updateMeetingStatus();
        return meetingPost.getIsMeetingFinish();
    }

    // 반경 n km 안에 존재하는 모임글들의 정보 반환 // 위치랑 meetingPost id
    @Transactional
    public ResponseEntity<?> meetingPostRadius(Double latitude, Double longitude, Double radius, List<String> categories) {

        return ResponseEntity.ok(meetingPostRepository.findByGeo(latitude, longitude, radius, categories));
    }

    @Transactional
    public ResponseEntity<BaseResponseDto> modify(MultipartFile mainImage,
                                                  MultipartFile subImage,
                                                  MultipartFile thirdImage,
                                                  WriteMeetingPostRequestDTO requestDTO, Long id) throws IOException {
        MeetingPost originalMP = meetingPostRepository.findOne(id);
        if (originalMP == null) {
            throw new IllegalArgumentException("수정에 실패했습니다.");
        }

        String mainImgUrl = "";
        String subImgUrl = "";
        String thirdImgUrl = "";

        if (mainImage != null) mainImgUrl = s3Service.uploadS3(mainImage, "images");
        if (mainImage != null) subImgUrl = s3Service.uploadS3(subImage, "images");
        if (mainImage != null) thirdImgUrl = s3Service.uploadS3(thirdImage, "images");

        meetingPostRepository.save(originalMP.updateMeetingPost(mainImgUrl, subImgUrl, thirdImgUrl, requestDTO));
        return ResponseEntity.ok(new BaseResponseDto(true, "수정에 성공했습니다."));
    }

    // 사용자 ID로 MeetingPost를 조회하고 DetailOfMeetingPostResponseDTO로 변환하는 메소드
    public List<DetailOfMeetingPostResponseDTO> getMeetingPostByUserId(Long userId) {
        List<MeetingPost> meetingPosts = meetingPostRepository.findByUserId(userId);
        return meetingPosts.stream()
                .map(meetingPost -> new DetailOfMeetingPostResponseDTO(meetingPost.getHead(), meetingPost))
                .collect(Collectors.toList());
    }


    //신청한 meetingPostId와 userId로 meetingApplier에서 해당 신청삭제
    @Transactional
    public void leaveMeeting(Long userId, Long meetingPostId){
        User findUser = userRepository.findOne(userId);
        List<MeetingApplier> meetingAppliers = findUser.getMeetingAppliers();
        for(MeetingApplier meetingApplier : meetingAppliers){
            if(meetingApplier.getMeetingPost().getId() == meetingPostId){
                meetingApplierRepository.leaveMeetingByUserIdAndMeetingPostId(meetingApplier);
            };
        }

    }

    @Transactional
    public void delete(Long id) {
        meetingPostRepository.deleteById(id);
    }
}
