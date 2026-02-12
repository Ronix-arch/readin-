package oth.ics.wtp.readinbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Notification;
import oth.ics.wtp.readinbackend.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // NOTE: This assumes @AuthenticationPrincipal resolves to your AppUser or a
    // UserDetails that you can use to fetch the AppUser.
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@AuthenticationPrincipal AppUser user) {
        // If your @AuthenticationPrincipal isn't AppUser automatically, you might need
        // to fetch it via UserService
        return ResponseEntity.ok(notificationService.getUserNotifications(user));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
