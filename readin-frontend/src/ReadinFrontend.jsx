import {useEffect, useState} from "react";
import Login from "./Login.jsx";
import Users from "./AppUsers.jsx";
import TimelinePosts from "./TimelinePosts.jsx";

import UserProfile from "./UserProfile.jsx";


export default function ReadinFrontend() {
    const [auth, setAuth] = useState({id: null, name: null, password: null, loggedIn: false});
    const [view, setView] = useState("login"); // Initial view is set to login
    const [profileUserId, setProfileUserId] = useState(null);
    const [userName, setUserName] = useState("");

    useEffect(() => {
        if (auth.loggedIn) {
            setView("timeline");
        }
    }, [auth.loggedIn]);
    const handleNavigateToProfile = (userId, userName) => {
        setProfileUserId(userId);// Set the ID of the user we want to see
        setUserName(userName); // Reset the username, if needed
        setView("userProfile");   // Switch the view to the profile component
    };

    return <>
        <header>
            <h2>ReadIn 🎭</h2>
            <nav
                aria-label="breadcrumb"
                style={{
                    "--pico-nav-breadcrumb-divider": "'✨'", display: "flex", justifyContent: "flex-end"

                }}
            >


                <ul>
                    <li><a href="#" className={view === "timeline" ? "current" : "default"}
                           onClick={() => setView("timeline")}>Timeline</a></li>
                    <li><a href="#" className={view === "users" ? "current" : "default"}
                           onClick={() => setView("users")}>Search Users</a></li>
                    <li><a href="#" className={view === "userProfile" ? "current" : "default"}
                           onClick={() => handleNavigateToProfile(auth.id, auth.name)}>My Profile</a></li>

                    <li><a href="#" className={view === "login" ? "current" : "default"}
                           onClick={() => setView("login")}>
                        {auth.loggedIn ? "Log out" : "Log in"}</a></li>
                </ul>

            </nav>
        </header>
        <main>
            {view === "timeline" ? <TimelinePosts auth={auth} userId={auth.id}/> : view === "users" ?
                <Users auth={auth} onNavigateToProfile={handleNavigateToProfile}/> : view === "userProfile" ?
                    <UserProfile
                        key={profileUserId} /* Add a key to force re-render */
                        auth={auth}
                        // You might need a way to get the username for the profileUserId
                        // For now, we pass the ID which is the most crucial part.
                        username={userName} /* This needs a better solution later */
                        userId={profileUserId} /* Pass the state ID, not the auth ID */
                        onNavigateToProfile={handleNavigateToProfile} /* Pass the handler down */
                    /> : <Login auth={auth} setAuth={setAuth}/>}

        </main>
        <footer>
            <p> Express Yourself. The Space is yours @ Readin </p>
            <p><small> Summer 2025, Web Technology Project, International Computer Science, OTH Regensburg</small></p>
        </footer>

    </>


}


