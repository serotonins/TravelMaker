package com.ssafy.gumibom.domain.record.service;

import com.ssafy.gumibom.domain.pamphlet.entity.PersonalPamphlet;
import com.ssafy.gumibom.domain.pamphlet.repository.PersonalPamphletRepository;
import com.ssafy.gumibom.domain.record.dto.PersonalRecordDto;
import com.ssafy.gumibom.domain.record.dto.request.SavePersonalRecordRequestDto;
import com.ssafy.gumibom.domain.record.dto.request.UpdatePersonalRecordRequestDto;
import com.ssafy.gumibom.domain.record.entity.PersonalRecord;
import com.ssafy.gumibom.domain.record.repository.RecordRepository;
import com.ssafy.gumibom.global.common.Emoji;
import com.ssafy.gumibom.global.service.S3Service;
//import com.ssafy.gumibom.global.util.ThumbnailProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final PersonalPamphletRepository pPamphletRepository;

    private final S3Service s3Service;

//    private final ThumbnailProvider thumbnailProvider;

    // 개인 팜플렛에 여행 기록 저장
    @Transactional
    public Long makePersonalRecord(MultipartFile image, MultipartFile video, SavePersonalRecordRequestDto dto) throws Exception {

        PersonalPamphlet pPamphlet = pPamphletRepository.findByPamphletId(dto.getPamphletId());
        String title = dto.getTitle();
        String imgUrl = "";
        String videoUrl = "";
        String videoThumbnailUrl = "";
        String text = dto.getText();
        Emoji emoji = dto.getEmoji();

        if(image!=null) imgUrl = s3Service.uploadS3(image, "images");
        if(video!=null) {
            videoUrl = s3Service.uploadS3(video, "videos");
//            videoThumbnailUrl = s3Service.uploadS3(thumbnailProvider.extractThumbnail(video), "images");
        }

        PersonalRecord pRecord = PersonalRecord.createPersonalRecord(title, imgUrl, videoUrl, videoThumbnailUrl, text, pPamphlet, emoji);
        recordRepository.save(pRecord);

        return pRecord.getId();
    }

    // 여행 기록 삭제
    @Transactional
    public void removePersonalRecord(Long pamphletId, Long recordId) throws Exception {

        PersonalPamphlet pPamphlet = pPamphletRepository.findByPamphletId(pamphletId);
        PersonalRecord pRecord = (PersonalRecord) recordRepository.findOne(recordId);

        String imgUrl = pRecord.getImgUrl();
        String videoUrl = pRecord.getVideoUrl();

        if(!Objects.equals(imgUrl, "")) s3Service.deleteS3(imgUrl);
        if(!Objects.equals(videoUrl, "")) s3Service.deleteS3(videoUrl);

        pPamphlet.removeRecord(pRecord);
        recordRepository.delete(pRecord);
    }

    // 여행 기록 수정
    @Transactional
    public void updatePersonalRecord(MultipartFile image, MultipartFile video, UpdatePersonalRecordRequestDto uPRRDto) throws Exception {

        PersonalPamphlet pPamphlet = pPamphletRepository.findByPamphletId(uPRRDto.getPamphletId());
        PersonalRecord pRecord = (PersonalRecord) recordRepository.findOne(uPRRDto.getRecordId());

        String imgUrl = "";
        String videoUrl = "";
        String videoThumbnailUrl = "";

        /**
         * 새로 들어온 파일이 있다면,
         * 1. S3에서 기존 파일 삭제
         * 2. S3에 새로운 파일 업로드
         */
        if(image!=null) {
            s3Service.deleteS3(pRecord.getImgUrl());
            imgUrl = s3Service.uploadS3(image, "images");
        }

        if(video!=null) {
            s3Service.deleteS3(pRecord.getVideoUrl());
            videoUrl = s3Service.uploadS3(video, "videos");
            s3Service.deleteS3(pRecord.getVideoThumbnailUrl());
//            videoThumbnailUrl = s3Service.uploadS3(thumbnailProvider.extractThumbnail(video), "images");
        }

        pRecord.updateRecord(uPRRDto.getTitle(), imgUrl, videoUrl, videoThumbnailUrl, uPRRDto.getText(), uPRRDto.getEmoji());
    }

    public List<PersonalRecordDto> showPersonalRecords(Long pamphletId) {
        return recordRepository.findByPamphletId(pamphletId)
                .stream().map(pr -> new PersonalRecordDto(pr))
                .collect(Collectors.toList());
    }

}
