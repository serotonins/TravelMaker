package com.ssafy.gumibom.domain.user.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsService {
    private final SmsCertificationUtil smsUtil;
    private final SmsRepository smsRepository;

    public void sendSms(SmsRequestDto requestDto){
        String to = requestDto.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendSms(to, certificationNumber);
        smsRepository.createSmsCertification(to,certificationNumber);
    }

    public void verifySms(SmsRequestDto requestDto) {
        if (isVerify(requestDto)) {
            throw new CustomExceptions.SmsCertificationNumberMismatchException("인증번호가 일치하지 않습니다.");
        }
        smsRepository.removeSmsCertification(requestDto.getPhone());
    }

    public boolean isVerify(SmsRequestDto requestDto) {
        return !(smsRepository.hasKey(requestDto.getPhone()) &&
                smsRepository.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }
}
