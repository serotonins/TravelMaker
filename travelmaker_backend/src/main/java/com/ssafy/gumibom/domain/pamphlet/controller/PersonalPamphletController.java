package com.ssafy.gumibom.domain.pamphlet.controller;

import com.ssafy.gumibom.domain.pamphlet.dto.PersonalPamphletDto;
import com.ssafy.gumibom.domain.pamphlet.dto.request.MakePersonalPamphletRequestDto;
import com.ssafy.gumibom.domain.pamphlet.dto.response.MakePersonalPamphletResponseDto;
import com.ssafy.gumibom.domain.pamphlet.entity.PersonalPamphlet;
import com.ssafy.gumibom.domain.pamphlet.repository.PersonalPamphletRepository;
import com.ssafy.gumibom.domain.pamphlet.service.PersonalPamphletService;
import com.ssafy.gumibom.global.base.BaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Personal Pamphlet", description = "개인 팜플렛 관련 api")
@RestController
@RequestMapping("/personal-pamphlet")
@RequiredArgsConstructor
public class PersonalPamphletController {

    private final PersonalPamphletService pPamphletService;

    @Operation(summary = "개인 팜플렛 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody MakePersonalPamphletResponseDto makePersonalPamphlet(
            @RequestPart MultipartFile image,
            @RequestPart MakePersonalPamphletRequestDto makePPReqDto) throws IOException {
        Long pId = pPamphletService.makePamphlet(image, makePPReqDto);

        return new MakePersonalPamphletResponseDto(true, "개인 팜플렛 생성 성공", pId);
    }

    @Operation(summary = "개인 팜플렛 종료")
    @PutMapping("/{pamphletId}")
    public ResponseEntity<BaseResponseDto> finishPersonalPamphlet(@PathVariable("pamphletId") Long pamphletId) {
        pPamphletService.finishPamphlet(pamphletId);
        return ResponseEntity.ok(new BaseResponseDto(true, "해당 개인 팜플렛이 종료되었습니다."));
    }

    @Operation(summary = "특정 개인 팜플렛 조회")
    @GetMapping(value = "v1/{pamphletId}")
    public PersonalPamphletDto selectPersonalPamphletById(@PathVariable("pamphletId") Long pamphletId) {
        return pPamphletService.selectPamphletById(pamphletId);
    }

    @Operation(summary = "특정 사용자의 개인 팜플렛 조회")
    @GetMapping(value = "v2/{userId}")
    public List<PersonalPamphletDto> selectPersonalPamphletByUserId(@PathVariable("userId") Long userId) {
        return pPamphletService.selectPamphletByUserId(userId);
    }

    @Operation(summary = "특정 사용자의 팜플렛을 제외한 모든 개인 팜플렛 조회")
    @GetMapping(value = "v3/{userId}")
    public List<PersonalPamphletDto> selectAllPersonalPamphlet(@PathVariable("userId") Long userId) {
        return pPamphletService.selectAllPamphlet(userId);
    }
}
