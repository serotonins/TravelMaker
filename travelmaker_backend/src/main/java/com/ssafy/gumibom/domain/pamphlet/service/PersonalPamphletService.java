package com.ssafy.gumibom.domain.pamphlet.service;

import com.ssafy.gumibom.domain.pamphlet.dto.PersonalPamphletDto;
import com.ssafy.gumibom.domain.pamphlet.dto.request.MakePersonalPamphletRequestDto;
import com.ssafy.gumibom.domain.pamphlet.entity.PersonalPamphlet;
import com.ssafy.gumibom.domain.pamphlet.repository.PersonalPamphletRepository;

import com.ssafy.gumibom.domain.user.entity.User;
import com.ssafy.gumibom.domain.user.repository.UserRepository;
import com.ssafy.gumibom.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonalPamphletService {

    private final PersonalPamphletRepository pPamphletRepository;
    private final UserRepository userRepository;

    private final S3Service s3Service;

    // 개인 팜플렛 생성
    @Transactional
    public Long makePamphlet(MultipartFile image, MakePersonalPamphletRequestDto makePPReqDto) throws IOException {

        User user = userRepository.findOne(makePPReqDto.getUserId());

        String imgUrl = "";
        if(image!=null) imgUrl = s3Service.uploadS3(image, "images");

        PersonalPamphlet pPamphlet = PersonalPamphlet.createPersonalPamphlet(user,
                makePPReqDto.getTitle(),
                makePPReqDto.getCategories(),
                imgUrl);

        return pPamphletRepository.save(pPamphlet);
    }

    // 개인 팜플렛 종료
    @Transactional
    public void finishPamphlet(Long pamphletId) {
        PersonalPamphlet pPamphlet = pPamphletRepository.findByPamphletId(pamphletId);
        pPamphlet.finishPamphlet();
    }

    // 팜플렛 아이디로 개인 팜플렛 조회
    public PersonalPamphletDto selectPamphletById(Long pamphletId) {
        PersonalPamphlet pamphlet = pPamphletRepository.findByPamphletId(pamphletId);
        return new PersonalPamphletDto(pamphlet);
    }

    // 유저 아이디로 개인 팜플렛 목록 조회
    public List<PersonalPamphletDto> selectPamphletByUserId(Long userId) {
        List<PersonalPamphlet> myPamphlets = pPamphletRepository.findByUserId(userId);
        List<PersonalPamphletDto> myPamphletsDto = myPamphlets.stream()
                .map(p -> new PersonalPamphletDto(p))
                .collect(Collectors.toList());

        return myPamphletsDto;
    }

    // 사용자의 개인 팜플렛을 제외한 모든 개인 팜플렛 조회
    public List<PersonalPamphletDto> selectAllPamphlet(Long userId) {
        List<PersonalPamphlet> myPersonalPamphlet = pPamphletRepository.findByUserId(userId);
        List<PersonalPamphlet> allPersonalPamphlet = pPamphletRepository.findAll();

        for(PersonalPamphlet myPamphlet: myPersonalPamphlet) {
            if(allPersonalPamphlet.contains(myPamphlet)) allPersonalPamphlet.remove(myPamphlet);
        }

        List<PersonalPamphletDto> allPersonPamphletDto = allPersonalPamphlet.stream()
                .map(p -> new PersonalPamphletDto(p))
                .collect(Collectors.toList());

        return allPersonPamphletDto;
    }
}
