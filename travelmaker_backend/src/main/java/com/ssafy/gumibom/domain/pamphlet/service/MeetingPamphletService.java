package com.ssafy.gumibom.domain.pamphlet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingPamphletService {

    public void makePamphlet() {

    }
}
