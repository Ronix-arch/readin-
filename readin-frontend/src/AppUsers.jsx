import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic} from "./Headers.js";

export  default  function Users ({auth}){
    const api = useContext(Api);
    const [users, setUsers] = useState([]);
    const[following, setFollowing] = useState(false); // not sure

    useEffect(()=>{
        fetch(api+ "appUsers",{headers: basic(auth)})
            .then(response =>{
                if(reponse.ok) return response.json();
                else throw new Error(response.statusText);
            }).then(result =>{
                setUsers(result);

        });
    },[api]);

    function appuserOnUsersList({user})// star from here

    function  followuser(followerId, followeeId){                                                           // is here header a right choice
        fetch( api + "/appUsers"+ followerId+ "/following/"+followeeId,{method: "POST", headers: basic(auth)})
        .then(response =>{
            if (!response.ok) throw new Error(response.statusText);
        }).then(()=>{
            setUsers(users);
        })

    }
    function  Unfollowuser(followerId, followeeId){
        fetch( api + "/appUsers"+ followerId+ "/following/"+followeeId,{method: "DELETE", headers: basic(auth)})
            .then(response =>{
                if (!response.ok) throw new Error(response.statusText);
            }).then(() =>{
                setUsers(users);

            });
    }
    function  isFollowing(followerId, followeeId){
        fetch( api + "/appUsers"+ followerId+ "/following/"+followeeId,{ headers: basic(auth)})
        .then(response =>{})

    }

    return <ul>{users.map( u =>
    <li key={u.id}>
        <div className= "grid">
            <p>{u.name}</p>


        </div>
    </li>)}
    </ul>

}