package com.example.chat.service;

import com.example.chat.model.ChatRoom;
import com.example.chat.model.Message;
import com.example.chat.repo.ChatRoomRepository;
import com.example.chat.repo.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository roomRepo;

    @Autowired
    private MessageRepository msgRepo;

    // ---------------- ROOMS ----------------

    public ChatRoom createRoom(String name) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        return roomRepo.save(room);
    }

    public List<ChatRoom> getAllRooms() {
        return roomRepo.findAll();
    }

    // ---------------- MESSAGES ----------------

    public Message saveMessage(String sender, String recipient, String content, boolean isRoomMessage) {

        Message msg = new Message();
        msg.setSender(sender);
        msg.setRecipient(recipient);
        msg.setContent(content);
        msg.setRoomMessage(isRoomMessage);
        msg.setTimestamp(Instant.now());

        return msgRepo.save(msg);
    }

    public List<Message> getRoomMessages(String roomName) {
        return msgRepo.findAll().stream()
                .filter(m -> m.isRoomMessage() && m.getRecipient().equals(roomName))
                .toList();
    }

    public List<Message> getPrivateMessages(String username) {
        return msgRepo.findAll().stream()
                .filter(msg ->
                        !msg.isRoomMessage() &&
                                (msg.getSender().equals(username) ||
                                 msg.getRecipient().equals(username))
                )
                .toList();
    }
}
