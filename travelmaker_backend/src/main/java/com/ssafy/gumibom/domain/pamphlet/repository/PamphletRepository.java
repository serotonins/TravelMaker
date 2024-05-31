package com.ssafy.gumibom.domain.pamphlet.repository;

import com.ssafy.gumibom.domain.pamphlet.entity.Pamphlet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PamphletRepository {

    public Long save(Pamphlet pamphlet);
    public List<Pamphlet> findByUserId(Long id);
    public List<Pamphlet> findAll();
}
