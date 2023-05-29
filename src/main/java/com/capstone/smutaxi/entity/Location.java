package com.capstone.smutaxi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
public class Location {

    //double에서 Double로 바꾼 이유는 double은 null을 허용하지 않아서
    private Double latitude; //위도
    private Double longitude; //경도

    public static double calculateDistance(Location userLocation, Location chatRoomLocation) {
        // 위도 경도를 이용한 거리 계산 알고리즘 구현
        // Haversine formula를 사용: 오차는 일반적으로 0.1% 이내로 유지되며 1km기준으로 약 1m의 오차 있음
        // 더 정확한 알고리즘으로는 Vincenty's formulae등이 있지만 사용하진 않을 듯?

        //chatRoomLocation이 null이면 아무도 들어있지 않은 채팅방 거리를 0으로 return 해서 매칭이 성사되도록 한다.
        if (chatRoomLocation == null) {
            return 0;
        }
        double userLatitude = userLocation.getLatitude();
        double userLongitude = userLocation.getLongitude();
        double chatRoomLatitude = chatRoomLocation.getLatitude();
        double chatRoomLongitude = chatRoomLocation.getLongitude();

        int earthRadius = 6371; // 지구 반지름 (단위: km)

        double dLat = Math.toRadians(chatRoomLatitude - userLatitude);
        double dLon = Math.toRadians(chatRoomLongitude - userLongitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(chatRoomLatitude))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadius * c * 1000; // 거리를 미터 단위로 변환

        return distance;
    }

}
