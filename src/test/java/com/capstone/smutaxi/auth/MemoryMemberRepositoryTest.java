package com.capstone.smutaxi.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class MemoryMemberRepositoryTest {

    MemoryUserRepoisitory repository = new MemoryUserRepoisitory();

    //Test는 순서를 보장하지 않음
    //순서에 의존하지 않는 테스트를 작성하자
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }
    @Test
    public void save() {
        //given
        User member = new User();
        member.setId("yellow7171");
        member.setPassword("1234");
        //when
        repository.save(member);
        //then
        User res = repository.findById("yellow7171").get();
        Assertions.assertThat(member).isEqualTo(res);

    }

    @Test
    public void findByName() {
        //given
        User m1 = new User();
        m1.setName("lkh");
        m1.setId("lkh");

        User m2 = new User();
        m2.setId("ksm");
        m2.setName("ksm");

        //when
        repository.save(m1);
        repository.save(m2);

        //then
        User res = repository.findByName("lkh").get();
        Assertions.assertThat(res).isEqualTo(m1);
    }

    @Test
    public void findAll() {
        //given
        User m1 = new User();
        m1.setId("1");

        User m2 = new User();
        m1.setId("2");

        //when
        repository.save(m1);
        repository.save(m2);

        //then
        List<User> res = repository.findAll();
        Assertions.assertThat(res.size()).isEqualTo(2);
    }

}
