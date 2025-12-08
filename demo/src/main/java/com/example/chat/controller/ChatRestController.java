package com.example.chat.controller;

import com.example.chat.model.ChatRoom;
import com.example.chat.model.Message;
import com.example.chat.service.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatRestController {

@Autowired
private ChatService chatService;

@PostMapping("/room")
public ChatRoom createRoom(@RequestBody ChatRoom room) {
    return chatService.createRoom(room.getName());
}

@GetMapping("/rooms")
public List<ChatRoom> getRooms() {
    return chatService.getAllRooms();
}

@GetMapping("/messages/room/{name}")
public List<Message> roomMessages(@PathVariable String name) {
    return chatService.getRoomMessages(name);
}

@GetMapping("/messages/private/{username}")
public List<Message> privateMessages(@PathVariable String username) {
    return chatService.getPrivateMessages(username);
}
}
