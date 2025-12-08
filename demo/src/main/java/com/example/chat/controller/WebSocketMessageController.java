package com.example.chat.controller;

import com.example.chat.dto.ChatMessageDTO;
import com.example.chat.service.ChatService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.stereotype.Controller;


@Controller
public class WebSocketMessageController {

    @Autowired
    private ChatService chatService;


    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDTO dto) {

      chatService.saveMessage(
      dto.getSender(),
      dto.getRecipient(),
      dto.getContent(),
      dto.isRoomMessage()
);

    }
}
