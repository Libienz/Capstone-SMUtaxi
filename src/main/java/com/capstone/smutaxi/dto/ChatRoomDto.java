package com.capstone.smutaxi.dto;

import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.utils.Location;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {
    private Long chatRoomId;
    private String chatRoomName;
    private List<Message> messageList;
    private Location chatRoomLocation;

}
