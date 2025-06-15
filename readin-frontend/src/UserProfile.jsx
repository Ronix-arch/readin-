import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic} from "./Headers.js";
import  UserProfilePosts from "./UserProfilePosts.jsx";



export default function UserProfile ({auth, username, useRId}) {
    const api = useContext(Api);
    const userId = useRId;
    const [followers, setFollowers] = useState([]);
    const [followees, setFollowees] = useState([]);
    const [followersCount, setFollowersCount] = useState(0);
    const [followeesCount, setFolloweesCount] = useState(0);







    useEffect(()=> {
        fetch(api + "/appUsers/" + userId + "/following/followers", {headers: basic(auth)})
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText);
            }).then(result => {
            setFollowers(result);
            setFollowersCount(result.length); // followers list is returned
        })
            .catch(error => console.error("Error fetching followers:", error));
    },[api, userId,auth])

    useEffect(()=> {
        fetch(api + "/appUsers/" + userId + "/following/followees", {headers: basic(auth)})
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText);
            }).then(result => {
            setFollowees(result);
            setFolloweesCount(result.length); // followers list is returned
        })
            .catch(error => console.error("Error fetching followees:", error));
    },[api, userId, auth])
  // STILL WORKING ON IT

return<>
    <div className="grid">
        <h3>Username: {username}</h3>


        <details className="dropdown">
            <summary role="button">
                followers: {followersCount}
            </summary>
            <ul>{followers.map(follower => (
                <li key={follower.id}>
                    <div className="grid">
                        <p>{follower.name}</p>
                        <button onClick={() => window.location.href = `/user/${follower.id}`}>See UserProfile</button>
                    </div>
                </li>
            ))} </ul>
        </details>

        <details className="dropdown">
            <summary role="button">
                followees: {followeesCount}
            </summary>
            <ul>{followees.map(followee => (
                <li key={followee.id}>
                    <div className="grid">
                        <p>{followee.name}</p>
                        <button onClick={() => window.location.href = `/user/${followee.id}`}>See UserProfile</button>
                    </div>
                </li>
            ))} </ul>
        </details>



    </div>


    <UserProfilePosts auth={auth} />



</>


}