package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.chat.MsgRoom;
import com.capstone.smutaxi.chat.MsgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class MsgController {

    private final MsgService msgService;

    @PostMapping
    public MsgRoom createRoom(@RequestParam String name){
        return msgService.createRoom(name);
    }

    @GetMapping
    public List<MsgRoom> findAllRoom(){
        return msgService.findAllRoom();
    }
}
