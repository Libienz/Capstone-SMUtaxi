package com.capstone.smutaxi.auth;

import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class JpaUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testUser() throws Exception {
        //given
        User user = new User();
        user.setEmail("yellow@gmail.com");
        userRepository.save(user);
        //when
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        //then

        Assertions.assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }
    @Test
    public void findByEmail() {
    }

    @Test
    public void save() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findByName() {
    }
}