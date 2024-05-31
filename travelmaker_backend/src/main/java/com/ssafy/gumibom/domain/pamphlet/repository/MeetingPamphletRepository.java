package com.ssafy.gumibom.domain.pamphlet.repository;

import com.ssafy.gumibom.domain.meeting.entity.Meeting;
import com.ssafy.gumibom.domain.meeting.repository.MeetingRepository;
import com.ssafy.gumibom.domain.pamphlet.entity.MeetingPamphlet;
import com.ssafy.gumibom.domain.pamphlet.entity.Pamphlet;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetingPamphletRepository {

    private final EntityManager em;
    private final MeetingRepository meetingRepository;

    public Long save(MeetingPamphlet mPamphlet) {
        em.persist(mPamphlet);
        return mPamphlet.getId();
    }

    public List<Pamphlet> findByUserId(Long userId) {
//        List<Meeting> meetings = meetingRepository.findByMemberId(userId);
//        // Pamphlet <-> MeetingPamphlet 상속 관계: pamphlet으로 형변환 해서 리턴 괜찮은지?
//        return meetings.stream().map(o -> (Pamphlet)o.getMeetingPamphlet()).toList();
        return null;
    }

    public List<Pamphlet> findAll() {
        return em.createQuery("select mp from MeetingPamphlet mp", Pamphlet.class).getResultList();
    }
}
