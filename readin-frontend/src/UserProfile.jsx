import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic} from "./Headers.js";
import  UserProfilePosts from "./UserProfilePosts.jsx";
import CreatePostCreation from "./Userpostcreation.jsx";



export default function UserProfile ({auth, username, useRId}) {
    const api = useContext(Api);
    const userId = useRId;
    const [followers, setFollowers] = useState([]);
    const [followees, setFollowees] = useState([]);
    const [followersCount, setFollowersCount] = useState(0);
    const [followeesCount, setFolloweesCount] = useState(0);
    const [isOpen, setIsOpen] = useState(false);







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
    },[api, userId])
  // STILL WORKING ON IT

return<>
    <div className="grid">
        <p>Username: {username}</p>
        <button onClick={() => setIsOpen(!isOpen)}>followers: {followersCount}</button>
        {isOpen && (  <ul className= "dropdown-menu">{followers.map(follower => (
            <li key={follower.id}>
                <div className="grid">
                    <p>{follower.name}</p>
                    <button onClick={<UserProfile auth={auth} username={follower.name} useRId={follower.id}/>}>See UserProfile</button>
                </div>
            </li>
        ))}</ul>)
        }

        <button onClick={() => setIsOpen(!isOpen)}>followees: {followeesCount}</button>
        {isOpen && (  <ul className= "dropdown-menu">{followees.map(followee => (
            <li key={followee.id}>
                <div className="grid">
                    <p>{followee.name}</p>
                    <button onClick={<UserProfile auth={auth} username={followee.name} useRId={followee.id}/>}>See UserProfile</button>
                </div>
            </li>
        ))}</ul>)
        }


    </div>
    {/*<CreatePostCreation auth={auth} /> //  same for this*/}

    <UserProfilePosts auth={auth}/>


</>



}