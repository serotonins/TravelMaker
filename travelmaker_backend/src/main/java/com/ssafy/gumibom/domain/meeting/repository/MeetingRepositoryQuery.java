package com.ssafy.gumibom.domain.meeting.repository;

import com.ssafy.gumibom.domain.meeting.entity.Meeting;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetingRepositoryQuery {
    private final EntityManager em;

    // userId로 Meeting 리스트 조회
    public List<Meeting> findByUserId(Long userId) {
        return em.createQuery(
                        "SELECT m FROM Meeting m " +
                                "JOIN FETCH m.meetingMembers mm " +
                                "WHERE mm.user.id = :userId", Meeting.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // meetingId로 Meeting 엔티티를 조회하고 반환
    public Meeting findByMeetingId(Long id){
        return em.find(Meeting.class, id);
    }
}


