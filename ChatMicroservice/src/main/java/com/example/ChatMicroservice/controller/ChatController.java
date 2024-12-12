package com.example.ChatMicroservice.controller;

import com.example.ChatMicroservice.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.time.ZoneId;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessageTemplate;
    private final String ADMIN_ID = "admin";

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessageTemplate) {
        this.simpMessageTemplate = simpMessageTemplate;
    }
//    @GetMapping("/getMessage")
//    public ResponseEntity<?> mesgTest(){
//        ChatMessage message= new ChatMessage();
//        Long time = System.currentTimeMillis(); // Autoboxing pentru conversie din long în Long
//        Instant now = Instant.now();
//        String formattedTimestamp = now.atZone(ZoneId.of("Europe/Bucharest"))
//                .toLocalDateTime()
//                .toString();
//        message.setTimestamp(formattedTimestamp); //
//
//        message.setSenderId(1L);
//        message.setReceiverId(2L);
//        message.setMessage("salut test");
//        simpMessageTemplate.convertAndSendToUser("sa", "/message", message);
//        return ResponseEntity.ok(message);
//    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage.getSenderId().equals(ADMIN_ID)) {
            simpMessageTemplate.convertAndSend("/msg/" + chatMessage.getReceiverId(), chatMessage);
        } else {
            simpMessageTemplate.convertAndSend("/msg/admin", chatMessage);
        }
    }

    @MessageMapping("/typing")
    public void notifyTyping(@Payload ChatMessage chatMessage) {
        if (chatMessage.getSenderId().equals(ADMIN_ID)) {
            simpMessageTemplate.convertAndSend("/notify/typing/" + chatMessage.getReceiverId(), chatMessage);
        } else {
            simpMessageTemplate.convertAndSend("/notify/admin/typing", chatMessage);
        }
    }

    @MessageMapping("/read")
    public void notifyRead(@Payload ChatMessage chatMessage) {
        if (chatMessage.getSenderId().equals(ADMIN_ID)) {
            simpMessageTemplate.convertAndSend("/notify/read/"+ chatMessage.getReceiverId(), chatMessage);
        } else {
            simpMessageTemplate.convertAndSend("/notify/admin/read", chatMessage);
        }
    }
}
