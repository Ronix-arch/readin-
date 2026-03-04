import { useState, useEffect } from "react";
import { Routes, Route, Link, useNavigate, Navigate } from "react-router-dom";
import Login from "./Login.jsx";
import Users from "./AppUsers.jsx";
import TimelinePosts from "./TimelinePosts.jsx";
import UserProfile from "./UserProfile.jsx";
import NotificationList from "./NotificationList.jsx";
import ChatWindow from "./ChatWindow.jsx";
import Inbox from "./Inbox.jsx";
import AdminDashboard from "./AdminDashboard.jsx"; // Added AdminDashboard
import { useParams } from "react-router-dom";

// Wrapper for ChatWindow to use useParams
function ChatWindowWrapper({ auth }) {
    const { userId } = useParams();
    return <ChatWindow auth={auth} otherUserId={userId} />;
}

// Wrapper for UserProfile to use useParams
function UserProfileWrapper({ auth }) {
    const { userId } = useParams();
    return <UserProfile auth={auth} userId={userId} />;
}

export default function ReadinFrontend() {
    const [auth, setAuth] = useState({ id: null, name: null, password: null, loggedIn: false });
    const navigate = useNavigate();

    useEffect(() => {
        if (auth.loggedIn) {
            navigate("/");
        } else {
            navigate("/login");
        }
    }, [auth.loggedIn]);

    const handleLogout = () => {
        setAuth({ id: null, name: null, password: null, loggedIn: false });
    };

    return (
        <>
            <header className="container">
                <nav>
                    <ul>
                        <li>
                            <Link to="/" style={{ display: "flex", alignItems: "center", textDecoration: "none" }}>
                                <img src="/appIcon.png" alt="ReadIn Icon" style={{ width: "40px", height: "40px", marginRight: "10px" }} />
                                <strong style={{ fontSize: "1.5rem", fontStyle: "italic" }}>ReadIn</strong>
                            </Link>
                        </li>
                    </ul>
                    {auth.loggedIn && (
                        <ul>
                            <li><Link to="/">Timeline</Link></li>
                            <li><Link to="/users">Search Users</Link></li>
                            <li><Link to="/messages" title="Inbox">Messages</Link></li>
                            <li><Link to={`/profile/${auth.id}`}>My Profile</Link></li>
                            <li><Link to="/notifications">Notifications</Link></li>
                            {auth.role === 'ADMIN' && <li><Link to="/admin">Admin Dashboard</Link></li>}
                            <li><a href="#" onClick={handleLogout}>Log out</a></li>
                        </ul>
                    )}
                </nav>
            </header>
            <main className="container">
                <Routes>
                    <Route path="/login" element={<Login setAuth={setAuth} />} />
                    {auth.loggedIn && (
                        <>
                            <Route path="/" element={<TimelinePosts auth={auth} userId={auth.id} />} />
                            <Route path="/users" element={<Users auth={auth} />} />
                            <Route path="/profile/:userId" element={<UserProfileWrapper auth={auth} />} />
                            <Route path="/notifications" element={<NotificationList auth={auth} />} />
                            <Route path="/messages" element={<Inbox auth={auth} />} />
                            <Route path="/messages/:userId" element={<ChatWindowWrapper auth={auth} />} />
                            {auth.role === 'ADMIN' && <Route path="/admin" element={<AdminDashboard auth={auth} />} />}
                        </>
                    )}
                    <Route path="*" element={<Navigate to={auth.loggedIn ? "/" : "/login"} />} />
                </Routes>
            </main>
            <footer className="container">
                <p> Express Yourself. The Space is yours @ Readin </p>
                <p><small>ReadIn is here. Let the world know a little bit about you.</small></p>
            </footer>
        </>
    );
}
