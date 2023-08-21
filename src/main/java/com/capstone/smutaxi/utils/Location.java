package com.capstone.smutaxi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    //double에서 Double로 바꾼 이유는 double은 null을 허용하지 않아서
    private Double latitude; //위도
    private Double longitude; //경도

    // 위도 경도를 이용한 거리 계산 알고리즘 구현
    // Haversine formula를 사용: 오차는 일반적으로 0.1% 이내로 유지되며 1km기준으로 약 1m의 오차 있음
    // 더 정확한 알고리즘으로는 Vincenty's formulae등이 있지만 무겁기에 사용하지 않음
    public static double calculateDistance(Location userLocation, Location waitingRoomLocation) {


        double userLatitude = userLocation.getLatitude();
        double userLongitude = userLocation.getLongitude();
        double waitingRoomLatitude = waitingRoomLocation.getLatitude();
        double waitingRoomLongitude = waitingRoomLocation.getLongitude();

        int earthRadius = 6371; // 지구 반지름 (단위: km)

        double dLat = Math.toRadians(waitingRoomLatitude - userLatitude);
        double dLon = Math.toRadians(waitingRoomLongitude - userLongitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(waitingRoomLatitude))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadius * c * 1000; // 거리를 미터 단위로 변환

        return distance;
    }

}
