import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic, basicJson} from "./Headers.js";
import CreatePostcreation from "./Userpostcreation.jsx";


export default function UserProfilePosts({auth}) {
    const api = useContext(Api);
    const userId = auth.id;
    const [posts, setPosts] = useState([]);
    const [editPostContent, setEditPostContent] = useState("");
    const [likeCounts, setLikeCounts] = useState({});  // IT TRACKS THE LIKES

    useEffect(() => {
        if (!userId) return;
        fetch(api + "/appUsers/"+ userId + "/posts/ownPosts", {headers: basic(auth)})
            .then(response =>{
                if(!response.ok) throw  new Error(response.statusText);
                return response.json();
            }).then(result =>{
            setPosts(result);
        })
            .catch(error => console.error("Error in fetching posts: ", error));


    },[api, auth,userId]);

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
// to make sure the number of likes is fetched after the posts load.
    useEffect(() => {
        if (posts.length > 0) {
            posts.forEach(post => numberOfLikePost(post.id));
        }
    }, [ posts]);





    function   updateuserposts(appUserId, postId){
        const updatedPost = {content: editPostContent};
        fetch(api + "/appUsers/"+appUserId+"/posts/"+postId, {headers: basicJson(auth),method: "PUT", body: JSON.stringify(updatedPost)})
            .then(response =>{
                if (response.ok) return response.json();
                else throw new Error(response.statusText);

            })
            .then(result =>{
                setPosts(posts.map(p=>(p.id === postId? result : p )));
            })
            .catch(error => console.error("Error in UPDATING  post: ", error));
    }

    function deletePost(appUserId, postId){
        fetch(api + "/appUsers/"+appUserId+"/posts/"+postId, {headers: basic(auth),method: "DELETE"})
            .then(response =>{
                if (!response.ok) throw  new Error(response.statusText);
            }).then(()=>{
                setPosts(prevPosts=> prevPosts.filter(p => p.id !== postId));
        }).catch(error => console.error("Error in deleting  post: ", error));

    }


return(
    <>
    <h2> User Own Posts </h2>
    <CreatePostcreation auth = {auth} updatePosts={setPosts} />
    <ul>
        {posts.map((p) => (
            <li key={p.id}>
                <p>{p.content}</p>
                <p>Number of Likes: {likeCounts[p.id]|| 0}</p>
                <div className= "grid">
                   <input  type ="text"
                           placeholder="Edit this Post Content "
                           value={editPostContent}
                           onChange={(e) => setEditPostContent(e.target.value)}
                   />
                    <button onClick={()=> updateuserposts(userId,p.id)}>Update Post</button>
                    <button onClick={()=>deletePost(userId,p.id)}>Delete Post</button>
                </div>


            </li>
        ))}
    </ul>
    </>


);




}