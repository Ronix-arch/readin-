package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Notification;
import oth.ics.wtp.readinbackend.repositories.NotificationRepository;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifications(AppUser user) {
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }

    public Notification createNotification(AppUser user, String type, String message, Long relatedEntityId) {
        Notification notification = new Notification(user, type, message, relatedEntityId);
        return notificationRepository.save(notification);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    @Transactional
    public void markAllAsRead(AppUser user) {
        List<Notification> unread = notificationRepository.findByUserAndIsReadFalseOrderByTimestampDesc(user);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }
}
