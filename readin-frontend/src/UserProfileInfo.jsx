import { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Api } from "./Context.js";
import { basic, basicJson } from "./Headers.js";

function EditProfileForm({ user, auth, onProfileUpdate }) {
    const api = useContext(Api);
    const [email, setEmail] = useState(user.email || "");
    const [bio, setBio] = useState(user.bio || "");
    const [file, setFile] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault();
        const formData = new FormData();
        const updateDto = { email, bio };
        formData.append("updateDto", new Blob([JSON.stringify(updateDto)], { type: "application/json" }));
        if (file) {
            formData.append("file", file);
        }

        fetch(`${api}/appUsers/${user.id}`, {
            method: "PUT",
            headers: basic(auth),
            body: formData,
        })
            .then(res => res.json())
            .then(updatedUser => {
                onProfileUpdate(updatedUser);
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>Email:
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
            </label>
            <label>Bio:
                <textarea value={bio} onChange={(e) => setBio(e.target.value)} />
            </label>
            <label>Profile Picture:
                <input type="file" onChange={(e) => setFile(e.target.files[0])} />
            </label>
            <button type="submit">Save</button>
        </form>
    );
}

export default function UserProfileInfo({ auth, userId }) {
    const api = useContext(Api);
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [followers, setFollowers] = useState([]);
    const [following, setFollowing] = useState([]);
    const [isFollowing, setIsFollowing] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [showFollowers, setShowFollowers] = useState(false);
    const [showFollowing, setShowFollowing] = useState(false);

    useEffect(() => {
        if (!userId) return;
        fetch(`${api}/appUsers/${userId}`, { headers: basic(auth) })
            .then(res => res.json())
            .then(setUser);

        fetch(`${api}/appUsers/${userId}/following/followers`, { headers: basic(auth) })
            .then(res => res.json())
            .then(setFollowers);

        fetch(`${api}/appUsers/${userId}/following/followees`, { headers: basic(auth) })
            .then(res => res.json())
            .then(setFollowing);

        if (String(auth.id) !== String(userId)) {
            fetch(`${api}/appUsers/${auth.id}/following/${userId}`, { headers: basic(auth) })
                .then(res => res.json())
                .then(setIsFollowing);
        }
    }, [api, auth, userId]);

    const handleFollow = () => {
        const method = isFollowing ? "DELETE" : "POST";
        fetch(`${api}/appUsers/${auth.id}/following/${userId}`, { method, headers: basic(auth) })
            .then(() => setIsFollowing(!isFollowing));
    };

    const handleProfileUpdate = (updatedUser) => {
        setUser(updatedUser);
        setIsEditing(false);
    };

    if (!user) return <div>Loading...</div>;

    return (
        <div>
            <h2>{user.name}'s Profile</h2>
            {user.profilePictureUrl && <img src={`${api}/files/${user.profilePictureUrl}`} alt="Profile" style={{ width: "150px", height: "150px", borderRadius: "50%" }} />}
            <p><strong>Email:</strong> {user.email}</p>
            <p><strong>Bio:</strong> {user.bio}</p>
            <p><strong>Joined:</strong> {new Date(user.createdAt).toLocaleDateString()}</p>

            {String(auth.id) === String(userId) ? (
                <button onClick={() => setIsEditing(!isEditing)}>
                    {isEditing ? "Cancel" : "Edit Profile"}
                </button>
            ) : (
                <>
                    <button onClick={handleFollow}>
                        {isFollowing ? "Unfollow" : "Follow"}
                    </button>
                    <button onClick={() => navigate(`/messages/${userId}`)} style={{ marginLeft: '10px' }}>
                        Message
                    </button>
                </>
            )}

            {isEditing && <EditProfileForm user={user} auth={auth} onProfileUpdate={handleProfileUpdate} />}

            <div>
                <button onClick={() => setShowFollowers(!showFollowers)}>
                    {showFollowers ? "Hide Followers" : `Followers (${followers.length})`}
                </button>
                {showFollowers && (
                    <ul>
                        {followers.map(f => <li key={f.id}><Link to={`/profile/${f.id}`}>{f.name}</Link></li>)}
                    </ul>
                )}
            </div>
            <div>
                <button onClick={() => setShowFollowing(!showFollowing)}>
                    {showFollowing ? "Hide Following" : `Following (${following.length})`}
                </button>
                {showFollowing && (
                    <ul>
                        {following.map(f => <li key={f.id}><Link to={`/profile/${f.id}`}>{f.name}</Link></li>)}
                    </ul>
                )}
            </div>
        </div>
    );
}
