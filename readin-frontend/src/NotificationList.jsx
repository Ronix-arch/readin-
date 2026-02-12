import { useContext, useEffect, useState } from "react";
import { Api } from "./Context.js";
import { basic } from "./Headers.js";

export default function NotificationList({ auth }) {
    const api = useContext(Api);
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        if (!auth.id) return;
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
