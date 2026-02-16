import { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Api } from "./Context.js";
import { basic } from "./Headers.js";

export default function Inbox({ auth }) {
    const api = useContext(Api);
    const [conversations, setConversations] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(`${api}/messages/conversations`, { headers: basic(auth) })
            .then(res => {
                if (!res.ok) {
                    throw new Error("Failed to fetch conversations");
                }
                return res.json();
            })
            .then(data => {
                setConversations(data);
                setLoading(false);
            })
            .catch(err => {
                console.error(err);
                setLoading(false);
            });
    }, [api, auth]);

    if (loading) return <div>Loading inbox...</div>;

    return (
        <div>
            <h1>Inbox</h1>
            {conversations.length === 0 ? (
                <p>No conversations yet. Start messaging someone from their profile!</p>
            ) : (
                <ul>
                    {conversations.map(user => (
                        <li key={user.id} style={{ marginBottom: "10px", padding: "10px", border: "1px solid #eee" }}>
                            <Link to={`/messages/${user.id}`} style={{ textDecoration: "none", color: "inherit", display: "flex", alignItems: "center" }}>
                                {user.profilePictureUrl && (
                                    <img
                                        src={`${api}/files/${user.profilePictureUrl}`}
                                        alt={user.name}
                                        style={{ width: "40px", height: "40px", borderRadius: "50%", marginRight: "10px" }}
                                    />
                                )}
                                <div>
                                    <strong>{user.name}</strong>
                                    {/* Potential future improvement: Show last message snippet here */}
                                </div>
                            </Link>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
