package com.ssafy.gumibom.domain.user.repository;

import com.ssafy.gumibom.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public void remove(User user) {
        em.remove(user);
    }

    public User findByUsername(String username) {
        try {
            return em.createQuery("select u from User u where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult(); // 리스트 대신 단일 결과를 가져옵니다.
        } catch (NoResultException e) {
            return null; // 결과가 없으면 null을 반환합니다.
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("중복된 사용자 아이디가 있습니다."); // 중복 결과 처리
        }
    }

    public Optional<User> findByPhoneNum(String phoneNum) {
        try {
            User user =  em.createQuery("select u from User u where u.phone = :phoneNum", User.class)
                    .setParameter("phoneNum", phoneNum)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("중복된 전화 번호가 있습니다.");
        }
    }

    public Boolean existUsersByPhoneNum(String phoneNum) {
        try {
            User user = em.createQuery("select u from User u where u.phone = :phoneNum", User.class)
                    .setParameter("phoneNum", phoneNum)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } catch (NonUniqueResultException e) {
//            throw new IllegalStateException("해당 전화번호는 이미 가입되어 있습니다.");
            return true;
        }
    }

    public void deleteByUsername(String username) {
        em.remove(findByUsername(username));
    }

    public Boolean existUsersByNickName(String nickName) {
        try {
            User user = em.createQuery("select u from User u where u.nickname = :nickName", User.class)
                    .setParameter("nickName", nickName)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } catch (NonUniqueResultException e) {
//            throw new IllegalStateException("해당 전화번호는 이미 가입되어 있습니다.");
            return true;
        }
    }

    public Boolean existUsersByLoginID(String loginID) {
        try {
            User user = em.createQuery("select u from User u where u.username = :loginID", User.class)
                    .setParameter("loginID", loginID)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } catch (NonUniqueResultException e) {
//            throw new IllegalStateException("해당 전화번호는 이미 가입되어 있습니다.");
            return true;
        }
    }


//    public void update(User user) {
//
//    }


}
