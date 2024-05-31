package com.ssafy.gumibom.domain.record.controller;

import com.ssafy.gumibom.domain.pamphlet.dto.request.MakePersonalPamphletRequestDto;
import com.ssafy.gumibom.domain.record.dto.PersonalRecordDto;
import com.ssafy.gumibom.domain.record.dto.request.DeletePersonalRecordRequestDto;
import com.ssafy.gumibom.domain.record.dto.request.SavePersonalRecordRequestDto;
import com.ssafy.gumibom.domain.record.dto.request.UpdatePersonalRecordRequestDto;
import com.ssafy.gumibom.domain.record.dto.response.SavePersonalRecordResponseDto;
import com.ssafy.gumibom.domain.record.entity.PersonalRecord;
import com.ssafy.gumibom.domain.record.entity.Record;
import com.ssafy.gumibom.domain.record.service.RecordService;
import com.ssafy.gumibom.global.base.BaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Response;
import retrofit2.http.Path;

import java.io.IOException;
import java.util.List;

@Tag(name = "Record", description = "여행 기록 관련 api")
@RestController
@RequestMapping("/personal-record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "개인 팜플렛에 여행 기록을 저장합니다.")
    @ResponseBody
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponseDto> savePersonalRecord(
            @RequestPart(required = false) MultipartFile image,
            @RequestPart(required = false) MultipartFile video,
            @RequestPart SavePersonalRecordRequestDto sPRRDto) throws Exception {

        Long recordId = recordService.makePersonalRecord(image, video, sPRRDto);

        return ResponseEntity.ok(new BaseResponseDto(true, "저장된 기록 id: "+recordId));
    }

    @Operation(summary = "개인 팜플렛에 저장된 여행 기록을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<BaseResponseDto> removePersonalRecord(@RequestBody DeletePersonalRecordRequestDto dPRRDto) throws Exception {

        recordService.removePersonalRecord(dPRRDto.getPamphletId(), dPRRDto.getRecordId());

        return ResponseEntity.ok(new BaseResponseDto(true, "여행 기록을 삭제했습니다."));
    }

    @Operation(summary = "개인 팜플렛에 저장된 여행 기록을 수정합니다.")
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponseDto> updatePersonalRecord(
            @RequestPart(required = false) MultipartFile image,
            @RequestPart(required = false) MultipartFile video,
            @RequestPart UpdatePersonalRecordRequestDto uPRRDto) throws Exception {

        recordService.updatePersonalRecord(image, video, uPRRDto);

        return ResponseEntity.ok(new BaseResponseDto(true, "여행 기록을 수정했습니다."));
    }

    @Operation(summary = "특정 개인 팜플렛의 여행 기록들을 조회합니다.")
    @GetMapping("/{pamphletId}")
    public ResponseEntity<List<PersonalRecordDto>> showPersonalRecords(@PathVariable("pamphletId") Long pamphletId) {

        return ResponseEntity.ok(recordService.showPersonalRecords(pamphletId));
    }


//    // S3 이미지 업로드 테스트용 API
//    @Operation(summary = "[테스트용] S3에 이미지를 업로드합니다. ")
//    @ResponseBody
//    @PostMapping(value = "/test/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public String uploadImg(HttpServletRequest request, @RequestParam(value = "image") MultipartFile image) throws IOException {
//        System.out.println(image);
//        System.out.println("=================");
//
//        return recordService.uploadImage(image);
//    }
//
//    // S3 비디오 업로드 테스트용 API
//    @Operation(summary = "[테스트용] S3에 비디오를 업로드합니다. ")
//    @ResponseBody
//    @PostMapping(value = "/test/upload/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public String uploadVideo(HttpServletRequest request, @RequestParam(value = "video") MultipartFile video) throws IOException {
//        System.out.println(video);
//        System.out.println("=================");
//
//        return recordService.uploadVideo(video);
//    }

}
