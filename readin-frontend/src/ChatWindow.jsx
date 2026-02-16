import { useContext, useEffect, useState, useRef } from "react";
import { Api } from "./Context.js";
import { basic } from "./Headers.js";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

export default function ChatWindow({ auth, otherUserId }) {
    const api = useContext(Api);
    const [messages, setMessages] = useState([]);
    const [msgContent, setMsgContent] = useState("");
    const stompClientRef = useRef(null);
    const [otherUser, setOtherUser] = useState(null);

    // Fetch other user details
    useEffect(() => {
        if (!otherUserId) return;
        fetch(api + "/appUsers/" + otherUserId, { headers: basic(auth) })
            .then(res => res.json())
            .then(data => setOtherUser(data))
            .catch(err => console.error("Error fetching user:", err));
    }, [api, auth, otherUserId]);

    // Connect WebSocket
    useEffect(() => {
        if (!auth.id) return;

        const client = new Client({
            // brokerURL: 'ws://localhost:8080/ws', // Not used with SockJS
            webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
            connectHeaders: {
                login: auth.name,
                passcode: auth.password
            },
            onConnect: () => {
                console.log("Connected to WebSocket");
                client.subscribe("/user/" + auth.name + "/queue/messages", (msg) => {
                    const receivedMsg = JSON.parse(msg.body);
                    // Only add if it belongs to this conversation
                    if (receivedMsg.sender.id === parseInt(otherUserId) || receivedMsg.receiver.id === parseInt(otherUserId)) {
                        setMessages(prev => [...prev, receivedMsg]);
                    }
                });
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            },
            debug: (str) => {
                console.log(str);
            },
            reconnectDelay: 5000,
        });

        client.activate();
        stompClientRef.current = client;

        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [auth, otherUserId]);

    // Fetch conversation history
    useEffect(() => {
        if (!otherUserId) return;
        fetch(api + "/messages/" + otherUserId, { headers: basic(auth) })
            .then(res => {
                if (!res.ok) {
                    throw new Error(`Status: ${res.status}`);
                }
                return res.json();
            })
            .then(data => {
                if (Array.isArray(data)) {
                    setMessages(data);
                } else {
                    console.error("Expected array of messages, got:", data);
                    setMessages([]);
                }
            })
            .catch(err => {
                console.error("Error fetching messages:", err);
                setMessages([]);
            });
    }, [api, auth, otherUserId]);

    const sendMessage = () => {
        if (!msgContent.trim() || !stompClientRef.current || !stompClientRef.current.connected) return;

        const message = {
            senderId: auth.id,
            receiverId: otherUserId,
            content: msgContent
        };

        stompClientRef.current.publish({
            destination: "/app/chat",
            body: JSON.stringify(message)
        });
        setMsgContent("");
    };

    return (
        <div className="container">
            <h3>Chat with {otherUser ? otherUser.name : "..."}</h3>
            <div style={{ height: '300px', overflowY: 'scroll', border: '1px solid #ccc', padding: '10px', marginBottom: '10px' }}>
                {messages.map((m, idx) => (
                    <div key={idx} style={{ textAlign: m.sender.id === auth.id ? 'right' : 'left', margin: '5px 0' }}>
                        <span style={{ background: m.sender.id === auth.id ? '#daf8cb' : '#f1f0f0', padding: '5px 10px', borderRadius: '5px' }}>
                            {m.content}
                        </span>
                    </div>
                ))}
            </div>
            <div style={{ display: 'flex' }}>
                <input
                    type="text"
                    value={msgContent}
                    onChange={e => setMsgContent(e.target.value)}
                    style={{ flexGrow: 1, marginRight: '5px' }}
                    placeholder="Type a message..."
                />
                <button onClick={sendMessage}>Send</button>
            </div>
        </div>
    );
}
