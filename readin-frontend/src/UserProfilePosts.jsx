import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic, basicJson} from "./Headers.js";
import CreatePostcreation from "./Userpostcreation.jsx";
import {hasUserLikedPost, likePost, numberOfLikePost, unlikePost} from "./TimelinePosts.jsx";


export default function UserProfilePosts({auth, userId}) {
    const api = useContext(Api);
    // const userId = auth.id;
    const [posts, setPosts] = useState([]);
    const [editPostContent, setEditPostContent] = useState("");
    const [likeCounts, setLikeCounts] = useState({});  // IT TRACKS THE LIKES
    const [likeStatus, setLikeStatus] = useState({});
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [numberOfPosts, setNumberOfPosts] = useState(0); // to track the number of posts

    useEffect(() => {
        if (!userId) return;
        fetchUsersOwnPosts(api, auth, userId, page)
            .then((data) => {
                setPosts((prev) => [...prev, ...data.content]); // `content` is from Spring Page
                setHasMore(!data.last); // indicates if more pages are available
                setNumberOfPosts(data.totalElements); // Set the total number of posts
            })
            .catch((error) => console.error("Error in fetching posts: ", error));
    }, [api, auth, userId, page]);


// to make sure the number of likes is fetched after the posts load.
    useEffect(() => {
        if (posts.length > 0) {
            posts.forEach(post => hasUserLikedPost(api, auth, setLikeStatus, userId, post.id));
            posts.forEach(post => numberOfLikePost(api, auth, setLikeCounts, post.id));
        }
    }, [posts]);


    function updateuserposts(appUserId, postId) {
        const updatedPost = {content: editPostContent};
        fetch(api + "/appUsers/" + appUserId + "/posts/" + postId, {
            headers: basicJson(auth),
            method: "PUT",
            body: JSON.stringify(updatedPost)
        })
            .then(response => {
                if (response.ok) return response.json(); else throw new Error(response.statusText);

            })
            .then(result => {
                setPosts(posts.map(p => (p.id === postId ? result : p)));
                setEditPostContent(""); // clear the input after  new
            })
            .catch(error => console.error("Error in UPDATING  post: ", error));
    }

    function deletePost(appUserId, postId) {
        fetch(api + "/appUsers/" + appUserId + "/posts/" + postId, {headers: basic(auth), method: "DELETE"})
            .then(response => {
                if (!response.ok) throw new Error(response.statusText);
            }).then(() => {
            setPosts(prevPosts => prevPosts.filter(p => p.id !== postId));
        }).catch(error => console.error("Error in deleting  post: ", error));

    }


    if (userId === auth.id) {
        return (<>
                <h2> Your have created sofar {numberOfPosts} Posts </h2>

                <CreatePostcreation auth={auth} updatePosts={setPosts}/>
                <h2> User profile(own) Posts 💬 </h2>
                <ul>
                    {posts.map((p) => (<li key={p.id}>
                            <p>{p.content}</p>
                            <p>Posted
                                on: {new Date(p.createdAt).toLocaleDateString()} at {new Date(p.createdAt).toLocaleTimeString()}</p>
                            <p>Number of Likes 👍: {likeCounts[p.id] || 0}</p>
                            <div className="grid">
                                <input type="text"
                                       placeholder="Edit this Post Content "
                                       value={editPostContent}
                                       onChange={(e) => setEditPostContent(e.target.value)}
                                />
                                <button onClick={() => updateuserposts(userId, p.id)}>Update Post</button>
                                <button onClick={() => deletePost(userId, p.id)}>Delete Post</button>
                            </div>


                        </li>))}
                </ul>
                {hasMore && (<button onClick={() => setPage((prev) => prev + 1)}>
                        Load More Posts.
                    </button>)}
            </>


        );

    } else {
        return (<>

                <h2> User profile(own) Posts </h2>
                <ul>
                    {posts.map((p) => (<li key={p.id}>
                            <p>{p.content}</p>
                            <p>Posted
                                on: {new Date(p.createdAt).toLocaleDateString()} at {new Date(p.createdAt).toLocaleTimeString()}</p>

                            <div className="grid">

                                <div className="grid">
                                    {likeStatus[p.id] !== undefined && (likeStatus[p.id] ? <button
                                                onClick={() => unlikePost(api, auth, setLikeStatus, setLikeCounts, auth.id, p.id)}> UnLike </button> :
                                            <button
                                                onClick={() => likePost(api, auth, setLikeStatus, setLikeCounts, auth.id, p.id)}>Like</button>

                                    )}
                                    <p>Number of likes 👍: {likeCounts[p.id] || 0}</p>
                                </div>
                            </div>


                        </li>))}
                </ul>
                {hasMore && (<button onClick={() => setPage((prev) => prev + 1)}>
                        Load More Posts.
                    </button>)}
            </>


        );
    }


}

export async function fetchUsersOwnPosts(api, auth, userId, page = 0, size = 20) {
    const res = await fetch(`${api}/appUsers/${userId}/posts/ownPosts?page=${page}&size=${size}`, {headers: basic(auth)});
    if (!res.ok) throw new Error("Failed to fetch ownposts");
    return res.json();
}
