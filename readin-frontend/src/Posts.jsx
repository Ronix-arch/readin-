import {useContext, useEffect, useRef, useState} from "react";
import {Api} from "./Context.js";
import {basic, basicJson} from "./Headers.js";

export default function Posts({auth}) {
    const api = useContext(Api);
    const userId = auth.id;
    const [posts, setPosts] = useState([]);
    //const [page, setPage] = useState(1);
    const [likes, setLikes] = useState([]);
    const [likeStatus, setLikeStatus] = useState({});

    useEffect(() => {
        fetch(api + "appUsers/ "+ userId + "/posts/timeLinePosts", {headers: basic(auth)})
            .then(response =>{
                if(!response.ok) throw  new Error(response.statusText);
                return response.json();
            }).then()  // start from here
    })


}