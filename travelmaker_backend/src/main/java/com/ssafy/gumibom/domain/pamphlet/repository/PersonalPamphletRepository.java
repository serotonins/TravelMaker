package com.ssafy.gumibom.domain.pamphlet.repository;

import com.ssafy.gumibom.domain.pamphlet.entity.Pamphlet;
import com.ssafy.gumibom.domain.pamphlet.entity.PersonalPamphlet;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonalPamphletRepository {

    private final EntityManager em;

    /**
     * Entity Manager가 관리하는 엔티티의 수정 방법 (2가지)
     * 1. 변경 감지 (dirty-checking)
     *  1-1. 영속성 컨텍스트에 종속된 엔티티에 대해 값을 바꿔주면 commit 시점에 update 쿼리가 나감 (자바의 컬렉션을 생각)
     * 2. 병합 (em.merge)
     *  2-1. 준영속 엔티티 -> 영속성 엔티티로 변경 (넘어온 모든 데이터를 바꿔치기)
     * 웬만하면 변경 감지를 이용할 것
     */
    public Long save(PersonalPamphlet pPamphlet) {
        em.persist(pPamphlet);
        return pPamphlet.getId();
    }

    public PersonalPamphlet findByPamphletId(Long id) {
//        return em.createQuery(
//                "select pp from PersonalPamphlet pp "+
//                        "join fetch pp.user "+
//                        "join fetch pp.title "+
//                        "join fetch pp.love "+
//                        "join fetch pp.createTime "+
//                        "join fetch pp.personalRecords ppr", PersonalPamphlet.class
//        )
//                .getResultList();
        return em.find(PersonalPamphlet.class, id);
    }

    public List<PersonalPamphlet> findByUserId(Long id) {
        return em.createQuery(
                "select pp from PersonalPamphlet pp " +
                        "join fetch pp.user u " +
                "where u.id = :id "
                        , PersonalPamphlet.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<PersonalPamphlet> findAll() {
        return em.createQuery(
                "select pp from PersonalPamphlet pp " +
                        "join fetch pp.user u " +
                        "where pp.isFinish = true"
                , PersonalPamphlet.class).getResultList();
    }
}
