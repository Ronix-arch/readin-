import { useContext, useEffect, useState } from "react";
import { Api } from "./Context.js";
import { basic, basicJson } from "./Headers.js";
import CreatePostcreation from "./Userpostcreation.jsx";
import { hasUserLikedPost, likePost, numberOfLikePost, unlikePost } from "./TimelinePosts.jsx";
import CommentSection from "./CommentSection.jsx";


export default function UserProfilePosts({ auth, userId }) {
    const api = useContext(Api);
    const [posts, setPosts] = useState([]);
    const [editPostContents, setEditPostContents] = useState({}); // Changed to an object
    const [likeCounts, setLikeCounts] = useState({});
    const [likeStatus, setLikeStatus] = useState({});
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [numberOfPosts, setNumberOfPosts] = useState(0);
    const [showComments, setShowComments] = useState({});

    useEffect(() => {
        if (!userId) return;
        fetchUsersOwnPosts(api, auth, userId, page)
            .then((data) => {
                setPosts((prev) => [...prev, ...data.content]);
                setHasMore(!data.last);
                setNumberOfPosts(data.totalElements);
            })
            .catch((error) => console.error("Error in fetching posts: ", error));
    }, [api, auth, userId, page]);


    useEffect(() => {
        if (posts.length > 0) {
            posts.forEach(post => hasUserLikedPost(api, auth, setLikeStatus, userId, post.id));
            posts.forEach(post => numberOfLikePost(api, auth, setLikeCounts, post.id));
        }
    }, [posts]);

    const toggleComments = (postId) => {
        setShowComments(prev => ({ ...prev, [postId]: !prev[postId] }));
    };

    function handleEditChange(postId, value) {
        setEditPostContents(prev => ({ ...prev, [postId]: value }));
    }

    function updateuserposts(appUserId, postId) {
        const updatedPost = { content: editPostContents[postId] || "" };
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
                handleEditChange(postId, ""); // Clear the specific input
            })
            .catch(error => console.error("Error in UPDATING  post: ", error));
    }

    function deletePost(appUserId, postId) {
        fetch(api + "/appUsers/" + appUserId + "/posts/" + postId, { headers: basic(auth), method: "DELETE" })
            .then(response => {
                if (!response.ok) throw new Error(response.statusText);
            }).then(() => {
                setPosts(prevPosts => prevPosts.filter(p => p.id !== postId));
            }).catch(error => console.error("Error in deleting  post: ", error));

    }


    if (String(userId) === String(auth.id)) {
        return (<>
            <h2> Your have created sofar {numberOfPosts} Posts </h2>

            <CreatePostcreation auth={auth} updatePosts={setPosts} />
            <h2> User profile(own) Posts 💬 </h2>
            {posts.map((p) => (
                <article key={p.id}>
                    <header>
                        <div style={{ display: "flex", alignItems: "center" }}>
                            {p.userProfilePictureUrl && <img src={`${api}/files/${p.userProfilePictureUrl}`} alt="Profile" style={{ width: "40px", height: "40px", borderRadius: "50%", marginRight: "10px" }} />}
                            <h5>@{p.userName}</h5>
                        </div>
                    </header>
                    <p>{p.content}</p>
                    {p.attachmentUrl && (
                        p.attachmentType.startsWith("image/") ? (
                            <img src={`${api}/files/${p.attachmentUrl}`} alt="Post attachment" style={{ maxWidth: "100%" }} />
                        ) : p.attachmentType.startsWith("video/") ? (
                            <video src={`${api}/files/${p.attachmentUrl}`} controls style={{ maxWidth: "100%" }} />
                        ) : null
                    )}
                    <footer>
                        <p>Posted
                            on: {new Date(p.createdAt).toLocaleDateString()} at {new Date(p.createdAt).toLocaleTimeString()}</p>
                        <p>Likes: {likeCounts[p.id] || 0}</p>
                        <div className="grid">
                            <input type="text"
                                placeholder="Edit this Post Content "
                                value={editPostContents[p.id] || ""}
                                onChange={(e) => handleEditChange(p.id, e.target.value)}
                            />
                            <button onClick={() => updateuserposts(userId, p.id)}>Update Post</button>
                            <button onClick={() => deletePost(userId, p.id)}>Delete Post</button>
                        </div>
                        <button onClick={() => toggleComments(p.id)}>
                            {showComments[p.id] ? "Hide Comments" : `Comments (${p.commentCount})`}
                        </button>
                        {showComments[p.id] && <CommentSection postId={p.id} auth={auth} />}
                    </footer>
                </article>
            ))}
            {hasMore && (<button onClick={() => setPage((prev) => prev + 1)}>
                Load More Posts.
            </button>)}
        </>


        );

    } else {
        return (<>

            <h2> User profile(own) Posts </h2>
            {posts.map((p) => (
                <article key={p.id}>
                    <header>
                        <div style={{ display: "flex", alignItems: "center" }}>
                            {p.userProfilePictureUrl && <img src={`${api}/files/${p.userProfilePictureUrl}`} alt="Profile" style={{ width: "40px", height: "40px", borderRadius: "50%", marginRight: "10px" }} />}
                            <h5>@{p.userName}</h5>
                        </div>
                    </header>
                    <p>{p.content}</p>
                    {p.attachmentUrl && (
                        p.attachmentType.startsWith("image/") ? (
                            <img src={`${api}/files/${p.attachmentUrl}`} alt="Post attachment" style={{ maxWidth: "100%" }} />
                        ) : p.attachmentType.startsWith("video/") ? (
                            <video src={`${api}/files/${p.attachmentUrl}`} controls style={{ maxWidth: "100%" }} />
                        ) : null
                    )}
                    <footer>
                        <p>Posted
                            on: {new Date(p.createdAt).toLocaleDateString()} at {new Date(p.createdAt).toLocaleTimeString()}</p>

                        <div className="grid">

                            <div className="grid">
                                {likeStatus[p.id] !== undefined && (likeStatus[p.id] ?
                                    <button onClick={() => unlikePost(api, auth, setLikeStatus, setLikeCounts, auth.id, p.id)}>❤️</button> :
                                    <button onClick={() => likePost(api, auth, setLikeStatus, setLikeCounts, auth.id, p.id)}>🤍</button>

                                )}
                                <p>Likes: {likeCounts[p.id] || 0}</p>
                            </div>
                            {auth.role === 'ADMIN' && <button onClick={() => deletePost(userId, p.id)} style={{ color: 'white', backgroundColor: 'red', marginTop: '10px' }}>Delete Post (Admin)</button>}
                            <button onClick={() => toggleComments(p.id)}>
                                {showComments[p.id] ? "Hide Comments" : `Comments (${p.commentCount})`}
                            </button>
                        </div>
                        {showComments[p.id] && <CommentSection postId={p.id} auth={auth} />}
                    </footer>
                </article>
            ))}
            {hasMore && (<button onClick={() => setPage((prev) => prev + 1)}>
                Load More Posts.
            </button>)}
        </>


        );
    }


}

export async function fetchUsersOwnPosts(api, auth, userId, page = 0, size = 20) {
    const res = await fetch(`${api}/appUsers/${userId}/posts/ownPosts?page=${page}&size=${size}`, { headers: basic(auth) });
    if (!res.ok) throw new Error("Failed to fetch ownposts");
    return res.json();
}
