package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.PrivateMessage;
import oth.ics.wtp.readinbackend.repositories.PrivateMessageRepository;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private PrivateMessageRepository messageRepository;

    public PrivateMessage saveMessage(PrivateMessage message) {
        return messageRepository.save(message);
    }

    public List<PrivateMessage> getConversation(AppUser user1, AppUser user2) {
        return messageRepository.findConversation(user1, user2);
    }

    public List<AppUser> getRecentConversations(AppUser user) {
        return messageRepository.findConversations(user);
    }

    @Transactional
    public void markMessagesAsRead(AppUser receiver, AppUser sender) {
        List<PrivateMessage> conversation = messageRepository.findConversation(receiver, sender); // Optimization needed
                                                                                                  // here for large
                                                                                                  // chats
        for (PrivateMessage msg : conversation) {
            if (msg.getReceiver().equals(receiver) && !msg.isRead()) {
                msg.setRead(true);
            }
        }
        messageRepository.saveAll(conversation);
    }
}
