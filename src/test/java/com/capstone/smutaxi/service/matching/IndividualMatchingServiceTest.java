package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.requests.match.MatchCancelRequest;
import com.capstone.smutaxi.dto.requests.match.MatchingRequest;
import com.capstone.smutaxi.dto.responses.match.MatchCancelResponse;
import com.capstone.smutaxi.dto.responses.match.MatchingResponse;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.entity.WaitingRoom;
import com.capstone.smutaxi.entity.WaitingRoomUser;
import com.capstone.smutaxi.enums.Gender;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.repository.WaitingRoomRepository;
import com.capstone.smutaxi.repository.WaitingRoomUserRepository;
import com.capstone.smutaxi.service.user.AuthService;
import com.capstone.smutaxi.utils.Location;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class IndividualMatchingServiceTest {

    @Autowired WaitingRoomRepository waitingRoomRepository;
    @Autowired MatchingService matchingService;
    @Autowired AuthService authService;
    @Autowired UserRepository userRepository;
    @Autowired
    WaitingRoomUserRepository waitingRoomUserRepository;


    @Test
    public void 빈대기방_위치값_삽입() throws Exception {
        //given
        User user = createUser("202010891@sangmyung.kr", "password123", "drake", Gender.MALE, null);
        authService.join(user.userToUserDto());
        MatchingRequest matchingRequest = createMatchingRequest(1.0, 1.0, "202010891@sangmyung.kr");

        //when
        Long waitingRoomId = matchingService.handleMatchingRequest(matchingRequest).getWaitingRoomId();
        WaitingRoom waitingRoom = waitingRoomRepository.findById(waitingRoomId);
        Double watingRoomLatitude = waitingRoom.getLocation().getLatitude();
        Double watingRoomLongitude = waitingRoom.getLocation().getLongitude();

        //then
        assertEquals(waitingRoom.getLocation().getLatitude(), matchingRequest.getLatitude());
        assertEquals(waitingRoom.getLocation().getLongitude(), matchingRequest.getLongitude());

    }

    @Test
    public void 대기방_인원_내림차순_정렬_확인() throws Exception {
        //given
        User user = createUser("202010891@sangmyung.kr", "password123", "drake", Gender.MALE, null);
        authService.join(user.userToUserDto());
        MatchingRequest matchingRequest = createMatchingRequest(1.0, 1.0, "202010891@sangmyung.kr");
        Long waitingRoomId = matchingService.handleMatchingRequest(matchingRequest).getWaitingRoomId();
        //when
        List<WaitingRoom> waitingRooms = waitingRoomRepository.findAll();
        //then
        WaitingRoom waitingRoom = waitingRooms.get(0);
        assertEquals(waitingRoom.getWaiters().size(), 1);

    }

    @Test
    public void 거리_기반_매칭_실패() throws Exception {
        //given
        User user1 = createUser("user1@sangmyung.kr", "password123", "drake", Gender.MALE, null);
        authService.join(user1.userToUserDto());
        User user2 = createUser("user2@sangmyung.kr", "password123", "drake", Gender.MALE, null);
        authService.join(user2.userToUserDto());

        //user1 위치 강남 교보문고
        Double aLat = 37.504030;
        Double aLong = 127.024099;
        Location kyobo = new Location(aLat, aLong);
        //user2 위치 강남역
        Double bLat = 37.497175;
        Double bLong = 127.027926;
        Location gangnam = new Location(bLat, bLong);

        //when: 강남역 <-> 교보문고 833m 이기에 이 두명의 매칭은 실패해야 함
        double distance = Location.calculateDistance(kyobo, gangnam);
        MatchingRequest matchingRequestA = createMatchingRequest(aLat, aLong, "user1@sangmyung.kr");
        MatchingRequest matchingRequestB = createMatchingRequest(bLat, bLong, "user2@sangmyung.kr");
        Long aId = matchingService.handleMatchingRequest(matchingRequestA).getWaitingRoomId();
        Long bId = matchingService.handleMatchingRequest(matchingRequestB).getWaitingRoomId();
        //then
        assertNotEquals(aId, bId);
     }

    @Test
    public void 거리_기반_매칭_성공() throws Exception {
        //given
        User user1 = createUser("user1@sangmyung.kr", "password123", "drake", Gender.MALE, null);
        authService.join(user1.userToUserDto());
        User user2 = createUser("user2@sangmyung.kr", "password123", "drake", Gender.MALE, null);
        authService.join(user2.userToUserDto());

        Double aLat = 37.566610;
        Double aLong = 126.977943;

        Double bLat = 37.566610;
        Double bLong = 126.977818;
        //when: a <-> b 11미터이기에 이 두명의 매칭은 성공해야 함
        double distance = Location.calculateDistance(new Location(aLat, aLong), new Location(bLat, bLong));
        MatchingRequest matchingRequestA = createMatchingRequest(aLat, aLong, "user1@sangmyung.kr");
        MatchingRequest matchingRequestB = createMatchingRequest(bLat, bLong, "user2@sangmyung.kr");
        Long aId = matchingService.handleMatchingRequest(matchingRequestA).getWaitingRoomId();
        Long bId = matchingService.handleMatchingRequest(matchingRequestB).getWaitingRoomId();
        //then
        assertEquals(aId, bId);
        WaitingRoom waitingRoom = waitingRoomRepository.findById(aId);
        List<WaitingRoomUser> waiters = waitingRoom.getWaiters();
        assertEquals(waiters.get(0).getUser().getEmail(), "user1@sangmyung.kr");
        assertEquals(waiters.get(1).getUser().getEmail(), "user2@sangmyung.kr");

    }


    @Test
    public void 연관관계_cascade_확인() {

        User user1 = createUser("user1@sangmyung.kr", "password123", "drake", Gender.MALE, null);
        authService.join(user1.userToUserDto());

        Double aLat = 37.566610;
        Double aLong = 126.977943;
        MatchingRequest matchingRequest = createMatchingRequest(aLat, aLong, "user1@sangmyung.kr");
        Long aId = matchingService.handleMatchingRequest(matchingRequest).getWaitingRoomId();
        WaitingRoom waitingRoom = waitingRoomRepository.findById(aId);
        waitingRoomUserRepository.delete(waitingRoom.getWaiters().get(0));

        Optional<User> byEmail = userRepository.findByEmail("user1@sangmyung.kr");
        assertEquals(byEmail.get().getEmail(),"user1@sangmyung.kr");


    }
    //매칭 취소 테스트들
    @Test
    public void 매칭_취소_확인() throws Exception {
        //given
        User user = createUser("123@abc.com", "1", "lee", Gender.MALE, null);
        authService.join(user.userToUserDto());

        Double aLat = 37.566610;
        Double aLong = 126.977943;
        MatchingRequest matchingRequest = createMatchingRequest(aLat, aLong, "123@abc.com");
        MatchingResponse matchingResponse = matchingService.handleMatchingRequest(matchingRequest);
        Long waitingRoomId = matchingResponse.getWaitingRoomId();
        Long waitingRoomUserId = matchingResponse.getWaitingRoomUserId();

        //when
        MatchCancelRequest matchCancelRequest = new MatchCancelRequest();
        matchCancelRequest.setWaitingRoomId(waitingRoomId);
        matchCancelRequest.setEmail(user.getEmail());
        matchCancelRequest.setWaitingRoomUserId(waitingRoomUserId);

        MatchCancelResponse matchCancelResponse = matchingService.cancelMatchRequest(matchCancelRequest);
        //then
        WaitingRoom canceledRoom = waitingRoomRepository.findById(waitingRoomId);
        assertEquals(canceledRoom.getWaiters().size(), 0);
        Optional<WaitingRoomUser> byId = waitingRoomUserRepository.findById(waitingRoomUserId);
        assertEquals(byId.isPresent(), false);

    }
    @NotNull
    private static MatchingRequest createMatchingRequest(Double latitude, Double longitude, String email) {
        MatchingRequest matchingRequest = new MatchingRequest();
        matchingRequest.setLatitude(latitude);
        matchingRequest.setLongitude(longitude);
        matchingRequest.setEmail(email);
        return matchingRequest;
    }

    private static User createUser(String email, String password, String name, Gender gender, String imageUrl) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setGender(gender);
        user.setImageUrl(imageUrl);
        user.getRoles().add("USER");
        return user;
    }


}