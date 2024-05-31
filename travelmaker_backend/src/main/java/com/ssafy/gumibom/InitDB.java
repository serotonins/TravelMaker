//package com.ssafy.gumibom;
//
//
//import com.ssafy.gumibom.domain.pamphlet.entity.PersonalPamphlet;
//import com.ssafy.gumibom.domain.user.entity.User;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//@Component
//@RequiredArgsConstructor
//public class InitDB {
//
//    private final InitDBService initDBService;
//
//    @PostConstruct
//    public void init() {
//        initDBService.initDB();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitDBService {
//
//        private final EntityManager em;
//
//        public void initDB() {
//            User user1 = createUser("중원1", "secret1", "jw1", "010-1111-1111");
//            User user2 = createUser("중원2", "secret2", "jw2", "010-2222-2222");
//            User user3 = createUser("중원3", "secret3", "jw3", "010-3333-3333");
//            em.persist(user1);
//            em.persist(user2);
//            em.persist(user3);
//
//            PersonalPamphlet personalPamphlet1 = PersonalPamphlet.createPersonalPamphlet(user1, "첫번째 개인 팜플렛!", new ArrayList<>(Arrays.asList("맛집", "힐링")));
//            PersonalPamphlet personalPamphlet2 = PersonalPamphlet.createPersonalPamphlet(user2, "두번째 개인 팜플렛!", new ArrayList<>(Arrays.asList("문화", "쇼핑")));
//            PersonalPamphlet personalPamphlet3 = PersonalPamphlet.createPersonalPamphlet(user3, "세번째 개인 팜플렛!", new ArrayList<>(Arrays.asList("맛집", "자연")));
//
//            em.persist(personalPamphlet1);
//            em.persist(personalPamphlet2);
//            em.persist(personalPamphlet3);
//        }
//    }
//
//    private static User createUser(String username, String password, String nickname, String phone) {
//        User user = new User(username, password, nickname, phone);
//        return user;
//    }
//}
