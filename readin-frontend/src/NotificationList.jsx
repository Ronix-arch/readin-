import { useContext, useEffect, useState } from "react";
import { Api } from "./Context.js";
import { basic } from "./Headers.js";

export default function NotificationList({ auth }) {
    const api = useContext(Api);
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        if (!auth.id) return;
        // Verify if context Api includes /api or not. 
        // In Login.jsx: fetch(api + "/appUsers/login"
        // In ChatWindow.jsx: fetch(api + "/messages/"
        // If api is "http://localhost", then we need "/api/notifications" if Nginx is stripping it, or "/notifications" if Nginx is gone?
        // Wait, I reverted Nginx to `proxy_pass http://backend:8080/;`. This STRIPS the matching part.
        // If location is `/api/`, then `http://localhost/api/notifications` becomes `http://backend:8080/notifications`.
        // So the backend path MUST be `/notifications`.
        // NotificationController has `@RequestMapping("/notifications")`. So that is correct.

        // However, let's check what `Api` context value is. 
        // Typically it is `http://localhost/api` or `http://localhost`.
        // If it is `http://localhost/api`, then `api + "/notifications"` = `http://localhost/api/notifications`. 
        // Request goes to Nginx `/api/notifications`. 
        // Nginx strips `/api/` -> `http://backend:8080/notifications`.
        // Backend handles `/notifications`. This seems correct.

        // BUT, if I changed ChatController to remove `/api`, maybe I should check if NotificationController needs change?
        // NotificationController is `@RequestMapping("/notifications")`. It does NOT have `/api`.
        // So `http://backend:8080/notifications` is correct.

        // The issue is likely the `@AuthenticationPrincipal AppUser` returning null, same as ChatController.

        fetch(api + "/notifications", { headers: basic(auth) })
            .then(res => res.json())
            .then(data => setNotifications(data))
            .catch(err => console.error("Error fetching notifications:", err));
    }, [api, auth]);

    const markAsRead = (id) => {
        fetch(api + "/notifications/" + id + "/read", { method: "POST", headers: basic(auth) })
            .then(res => {
                if (res.ok) {
                    setNotifications(prev => prev.map(n => n.id === id ? { ...n, read: true } : n));
                }
            })
            .catch(err => console.error("Error marking notification as read:", err));
    };

    return (
        <div className="container">
            <h2>Notifications 🔔</h2>
            {notifications.length === 0 ? <p>No notifications.</p> : (
                <ul className="list-group">
                    {notifications.map(n => (
                        <li key={n.id} style={{ opacity: n.read ? 0.6 : 1, padding: '10px', borderBottom: '1px solid #ccc' }}>
                            <strong>{n.type}</strong>: {n.message}
                            <br />
                            <small>{new Date(n.timestamp).toLocaleString()}</small>
                            {!n.read && <button onClick={() => markAsRead(n.id)} style={{ marginLeft: '10px' }}>Mark as read</button>}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
