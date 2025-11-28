import { useEffect, useState } from "react";
import Login from "./Login.jsx";
import Users from "./AppUsers.jsx";
import TimelinePosts from "./TimelinePosts.jsx";
import UserProfile from "./UserProfile.jsx";

export default function ReadinFrontend() {
    const [auth, setAuth] = useState({ id: null, name: null, password: null, loggedIn: false });
    const [view, setView] = useState("login");
    const [profileUserId, setProfileUserId] = useState(null);

    useEffect(() => {
        if (auth.loggedIn) {
            setView("timeline");
        } else {
            setView("login");
        }
    }, [auth.loggedIn]);

    const handleNavigateToProfile = (userId) => {
        setProfileUserId(userId);
        setView("userProfile");
    };

    const handleLogout = () => {
        setAuth({ id: null, name: null, password: null, loggedIn: false });
    };

    return (
        <>
            <header className="container">
                <nav>
                    <ul>
                        <li><strong>ReadIn 🎭</strong></li>
                    </ul>
                    {auth.loggedIn && (
                        <ul>
                            <li><a href="#" onClick={() => setView("timeline")}>Timeline</a></li>
                            <li><a href="#" onClick={() => setView("users")}>Search Users</a></li>
                            <li><a href="#" onClick={() => handleNavigateToProfile(auth.id)}>My Profile</a></li>
                            <li><a href="#" onClick={handleLogout}>Log out</a></li>
                        </ul>
                    )}
                </nav>
            </header>
            <main className="container">
                {view === "login" && <Login setAuth={setAuth} />}
                {view === "timeline" && <TimelinePosts auth={auth} userId={auth.id} />}
                {view === "users" && <Users auth={auth} onNavigateToProfile={handleNavigateToProfile} />}
                {view === "userProfile" && <UserProfile auth={auth} userId={profileUserId} onNavigateToProfile={handleNavigateToProfile} />}
            </main>
            <footer className="container">
                <p> Express Yourself. The Space is yours @ Readin </p>
                <p><small> Summer 2025, Web Technology Project, International Computer Science, OTH Regensburg</small></p>
            </footer>
        </>
    );
}