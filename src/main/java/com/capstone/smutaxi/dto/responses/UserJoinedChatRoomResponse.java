package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.UserJoinedChatRoomDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinedChatRoomResponse {
    private Boolean success;
    private String message;
    private List<UserJoinedChatRoomDto> userJoinedChatRoomDtoList;
}
