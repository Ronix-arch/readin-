import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic} from "./Headers.js";

export  default  function Users ({auth}){
    const api = useContext(Api);
    const [users, setUsers] = useState([]);
    const[followingStatus, setFollowingStatus] = useState({}); // not sure

    useEffect(()=>{
        fetch(api+ "/appUsers",{headers: basic(auth)})
            .then(response =>{
                if(response.ok) return response.json();
                else throw new Error(response.statusText);
            }).then(result =>{
                setUsers(result);

        });
    },[api]);

    function  isFollowing(followerId, followeeId){
        fetch( api + "/appUsers"+ followerId+ "/following/"+followeeId,{ headers: basic(auth)})
            .then(response =>{
                if(!response.ok) throw new Error(response.statusText);
                return response.json();      // we expect a boolen value.
            }).then(isFollowing => {
                setFollowingStatus(prev =>({...prev,[followeeId]: isFollowing}));
        })
            .catch(error => console.error("Error in checking follow status",error));

    }

    function  followuser(followerId, followeeId){                                                           // is here header a right choice
        fetch( api + "/appUsers"+ followerId+ "/following/"+followeeId,{method: "POST", headers: basic(auth)})
        .then(response =>{
            if (!response.ok) throw new Error(response.statusText);
            return isFollowing(followerId, followeeId);
        }).catch(error => console.error("Error following user:", error));



}
    function  unfollowuser(followerId, followeeId){
        fetch( api + "/appUsers"+ followerId+ "/following/"+followeeId,{method: "DELETE", headers: basic(auth)})
            .then(response =>{
                if (!response.ok) throw new Error(response.statusText);
                return isFollowing(followerId, followeeId);
            }).catch(error => console.error("Error unfollowing user:", error));

}


    return (
    <ul>{users.map( u =>
    <li key={u.id}>
        <div className= "grid">
            <p>{u.name}</p>
            {followingStatus[u.id] !== undefined && (
                followingStatus[u.id] ? (<button onClick ={() => unfollowuser(auth.id ,u.id)}>Unfollow</button>) : (
                    <button onClick ={() => followuser(auth.id ,u.id)}>follow</button>
                )
            ) }


        </div>
    </li>)}
    </ul>
    )

}