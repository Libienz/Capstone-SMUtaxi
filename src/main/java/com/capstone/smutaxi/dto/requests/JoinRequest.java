package com.capstone.smutaxi.dto.requests;

import com.capstone.smutaxi.enums.Gender;
import lombok.Getter;

@Getter
public class JoinRequest {

    private String email;
    private String password;
    private String name;
    private Gender gender;
//    private MultipartFile img;
    /**
     * @img
     * 전송 성능을 생각하면 MultipartFile을 사용해야 함 이를 위해선 api 엔드포인트를 하나 추가해야 함 form json으로는 받을 수 없기 때문
     * 즉 두개의 api가 join하는데에 필요하다.
     * 반면 byte[]는 json form body에 담길 수 있지만 복잡하고 데이터 크기가 증가할 수 있음
     * 이 부분을 정하고 처리해야 함! 우선
     */


}
