package com.capstone.smutaxi.dto;

import com.capstone.smutaxi.entity.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserJoinedChatRoomDto {
    private Long chatRoomId;
    private String chatRoomName;
    private String lastMessage;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastSentTime;
    private List<UserDto> participants;
    private Integer nonReadMessageCount;

}
