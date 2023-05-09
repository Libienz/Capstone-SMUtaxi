package com.capstone.smutaxi.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Optional;



class MemoryMemberRepositoryTest {

    MemoryMemberRepoisitory repository = new MemoryMemberRepoisitory();

    //Test는 순서를 보장하지 않음
    //순서에 의존하지 않는 테스트를 작성하자
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }
    @Test
    public void save() {
        //given
        Member member = new Member();
        member.setId("yellow7171");
        member.setPassword("1234");
        //when
        repository.save(member);
        //then
        Member res = repository.findById("yellow7171").get();
        Assertions.assertThat(member).isEqualTo(res);

    }

    @Test
    public void findByName() {
        //given
        Member m1 = new Member();
        m1.setName("lkh");
        m1.setId("lkh");

        Member m2 = new Member();
        m2.setId("ksm");
        m2.setName("ksm");

        //when
        repository.save(m1);
        repository.save(m2);

        //then
        Member res = repository.findByName("lkh").get();
        Assertions.assertThat(res).isEqualTo(m1);
    }

    @Test
    public void findAll() {
        //given
        Member m1 = new Member();
        m1.setId("1");

        Member m2 = new Member();
        m1.setId("2");

        //when
        repository.save(m1);
        repository.save(m2);

        //then
        List<Member> res = repository.findAll();
        Assertions.assertThat(res.size()).isEqualTo(2);
    }

}
