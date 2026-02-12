package oth.ics.wtp.readinbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import oth.ics.wtp.readinbackend.entities.Notification;
import oth.ics.wtp.readinbackend.entities.AppUser;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByTimestampDesc(AppUser user);

    List<Notification> findByUserAndIsReadFalseOrderByTimestampDesc(AppUser user);
}
