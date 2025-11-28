import UserProfilePosts from "./UserProfilePosts.jsx";
import UserProfileInfo from "./UserProfileInfo.jsx";

export default function UserProfile({ auth, userId, onNavigateToProfile }) {
    return (
        <>
            <h1>User Profile</h1>
            <UserProfileInfo auth={auth} userId={userId} onNavigateToProfile={onNavigateToProfile} />
            <UserProfilePosts auth={auth} userId={userId} />
        </>
    );
}
