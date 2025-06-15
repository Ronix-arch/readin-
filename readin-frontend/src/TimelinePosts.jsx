import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic} from "./Headers.js";
import CreatePostcreation from "./Userpostcreation.jsx"; // i might leave it out not so important.

export default function TimelinePosts({auth}) {
    const api = useContext(Api);
    const userId = auth.id;
    const [posts, setPosts] = useState([]);
    //const [page, setPage] = useState(1);
    const [likeCounts, setLikeCounts] = useState({});


    const [likeStatus, setLikeStatus] = useState({});

    useEffect(() => {
        if (!userId) return;
        fetch(api + "/appUsers/"+ userId + "/posts/timeLinePosts", {headers: basic(auth)})
            .then(response =>{
                if(!response.ok) throw  new Error(response.statusText);
                return response.json();
            }).then(result =>{
            setPosts(result);
        })
            .catch(error => console.error("Error in fetching posts: ", error));


    },[api, userId,auth]);

//
    function numberOfLikePost (postId) {
        fetch(api + "/appUsers/posts/"+ postId +"/likeCount",{headers: basic(auth)})
            .then(response =>{
                if(!response.ok) throw  new Error(response.statusText);
                return response.json();
            }).then(likeCount => {
            setLikeCounts(prev => ({...prev, [postId]: likeCount}));
        })
            .catch(error => console.error("Error in getting no of likes of a post: ", error));
    }

    useEffect(() => {
        if(posts.length > 0) {
            posts.forEach(post => hasUserLikedPost(userId, post.id));
            posts.forEach(post => numberOfLikePost(post.id));
        }//  to Ensure like count is fetched after posts load
    }, [ posts]);
    


    function hasUserLikedPost (appUserId, postId) {
        fetch(api + "/appUsers/"+appUserId +"/posts/"+postId+"/like", {headers: basic(auth)})
            .then(response =>{
                if(!response.ok) throw  new Error(response.statusText);
                return response.json();
            }).then(hasUserLikedPost => {
            setLikeStatus(prev => ({...prev, [postId]: hasUserLikedPost}));
        })
            .catch(error => console.error("Error in checking like status",error));
    }

    function likePost (appUserId, postId) {
        fetch(api + "/appUsers/"+appUserId +"/posts/"+postId+"/like", {method: "POST",headers: basic(auth)})
            .then(response =>{
                if(!response.ok) throw  new Error(response.statusText);
                setLikeStatus(prev => ({ ...prev, [postId]: true }));
                setLikeCounts(prev => ({ ...prev, [postId]: (prev[postId] || 0) + 1 }));


               // return hasUserLikedPost(appUserId, postId);  not needed any more
            })
            .catch(error => console.error("Error in liking  post: ", error));
    }

    function unlikePost (appUserId, postId) {
        fetch(api + "/appUsers/"+appUserId +"/posts/"+postId+"/like", {method: "DELETE",headers: basic(auth)})
            .then(response =>{
                if(!response.ok) throw  new Error(response.statusText);
                setLikeStatus(prev => ({ ...prev, [postId]: false }));
                setLikeCounts(prev => ({ ...prev, [postId]: Math.max((prev[postId] || 0) - 1, 0) })); // Avoid negative counts
            })


            // not needed any more return hasUserLikedPost(appUserId, postId);})
            .catch(error => console.error("Error in unliking  post: ", error));
    }

    

    return (
        <>
            <h2> Your Followees' Posts </h2>
            {/*<CreatePostcreation auth = {auth} updatePosts={setPosts()} /> How to solve this problem */}
        <ul>{posts.map( p =>
            <li key ={p.id}>
                <p>{p.content}</p>
                <p>Posted on: {new Date(p.createdAt).toLocaleDateString()} at {new Date(p.createdAt).toLocaleTimeString()}</p>

                <div className="grid">
                    {likeStatus[p.id] !== undefined &&(
                        likeStatus[p.id] ?<button onClick={()=> unlikePost(userId, p.id)}> UnLike </button> :
                            <button onClick={()=> likePost(userId, p.id)}>Like</button>

                    )}
                    <p>Number of likes: {likeCounts[p.id] || 0}</p>
                </div>
            </li>)}




        </ul>
            </>
    )


}