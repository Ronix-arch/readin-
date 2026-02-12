package oth.ics.wtp.readinbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody; // For REST endpoints mixed in
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.PrivateMessage;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;
import oth.ics.wtp.readinbackend.services.MessageService;

import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AppUserRepository appUserRepository;

    // REST endpoint to get conversation history
    @GetMapping("/api/messages/{userId}")
    @ResponseBody
    public ResponseEntity<List<PrivateMessage>> getConversation(@AuthenticationPrincipal AppUser currentUser,
            @PathVariable Long userId) {
        AppUser otherUser = appUserRepository.findById(userId).orElse(null);
        if (otherUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messageService.getConversation(currentUser, otherUser));
    }

    // WebSocket endpoint: /app/chat
    @MessageMapping("/chat")
    public void processMessage(@Payload Map<String, Object> payload) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        String content = (String) payload.get("content");

        AppUser sender = appUserRepository.findById(senderId).orElse(null);
        AppUser receiver = appUserRepository.findById(receiverId).orElse(null);

        if (sender != null && receiver != null) {
            PrivateMessage savedMsg = messageService.saveMessage(new PrivateMessage(sender, receiver, content));

            // Send to receiver
            messagingTemplate.convertAndSendToUser(
                    receiver.getName(), "/queue/messages",
                    savedMsg);
            // Include yourself so you see the message confirmed (optional if frontend
            // handles it optimistically)
            messagingTemplate.convertAndSendToUser(
                    sender.getName(), "/queue/messages",
                    savedMsg);
        }
    }
}
