package com.ssafy.gumibom.domain.record.repository;

import com.ssafy.gumibom.domain.record.entity.PersonalRecord;
import com.ssafy.gumibom.domain.record.entity.Record;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecordRepository {

    private final EntityManager em;

    public void save(Record record) {
        em.persist(record);
    }

    public void delete(Record record) { em.remove(record); }

    public Record findOne(Long id) {
        return em.find(Record.class, id);
    }

    public List<PersonalRecord> findByPamphletId(Long id) {
        return em.createQuery(
                "select pr from PersonalRecord pr "+
                        "where pr.personalPamphlet.id=:id", PersonalRecord.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Record> findAll() {
        return em.createQuery("select r from Record r", Record.class).getResultList();
    }
}
