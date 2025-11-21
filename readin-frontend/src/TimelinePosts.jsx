import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic} from "./Headers.js";

export default function TimelinePosts({auth, userId}) {
    const api = useContext(Api);

    const [posts, setPosts] = useState([]);
    //const [page, setPage] = useState(1);
    const [likeCounts, setLikeCounts] = useState({});


    const [likeStatus, setLikeStatus] = useState({});

    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    useEffect(() => {
        if (!userId) return;
        fetchTimelinePosts(api, auth, userId, page)
            .then((data) => {
                setPosts((prev) => [...prev, ...data.content]); // `content` is from Spring Page
                setHasMore(!data.last); // indicates if more pages are available
            })
            .catch((error) => console.error("Error in fetching posts: ", error));
    }, [api, auth, userId, page]);


    useEffect(() => {
        if (posts.length > 0) {
            posts.forEach(post => hasUserLikedPost(api, auth, setLikeStatus, userId, post.id));
            posts.forEach(post => numberOfLikePost(api, auth, setLikeCounts, post.id));
        }//  to Ensure like count is fetched after posts load
    }, [posts]);


    return (<>
            <h2> Your Followees' Posts 💬</h2>
            {posts.map(p => (
                <article key={p.id}>
                    <header>
                        <h5>@{p.userName}</h5>
                    </header>
                    <p>{p.content}</p>
                    {p.attachmentUrl && (
                        p.attachmentType.startsWith("image/") ? (
                            <img src={`${api}/files/${p.attachmentUrl}`} alt="Post attachment" style={{maxWidth: "100%"}}/>
                        ) : p.attachmentType.startsWith("video/") ? (
                            <video src={`${api}/files/${p.attachmentUrl}`} controls style={{maxWidth: "100%"}}/>
                        ) : null
                    )}
                    <footer>
                        <p>Posted
                            on: {new Date(p.createdAt).toLocaleDateString()} at {new Date(p.createdAt).toLocaleTimeString()}</p>

                        <div className="grid">
                            {likeStatus[p.id] !== undefined && (likeStatus[p.id] ?
                                <button onClick={() => unlikePost(api, auth, setLikeStatus, setLikeCounts, userId, p.id)}> ❤️UnLike </button> :
                                <button onClick={() => likePost(api, auth, setLikeStatus, setLikeCounts, userId, p.id)}> 🤍
                                    Like</button>

                            )}
                            <p>Number of likes 👍: {likeCounts[p.id] || 0}</p>
                        </div>
                    </footer>
                </article>
            ))}
            {hasMore && (<button onClick={() => setPage((prev) => prev + 1)}>
                    Load More Posts.
                </button>)}

        </>)


}

export async function fetchTimelinePosts(api, auth, userId, page = 0, size = 20) {
    const res = await fetch(`${api}/appUsers/${userId}/posts/timeLinePosts?page=${page}&size=${size}`, {headers: basic(auth)});
    if (!res.ok) throw new Error("Failed to fetch timeline posts");
    return res.json();
}

export function likePost(api, auth, setLikeStatus, setLikeCounts, appUserId, postId) {
    fetch(api + "/appUsers/" + appUserId + "/posts/" + postId + "/like", {method: "POST", headers: basic(auth)})
        .then(response => {
            if (!response.ok) throw new Error(response.statusText);
            setLikeStatus(prev => ({...prev, [postId]: true}));
            setLikeCounts(prev => ({...prev, [postId]: (prev[postId] || 0) + 1}));
        })
        .catch(error => console.error("Error in liking  post: ", error));
}

export function unlikePost(api, auth, setLikeStatus, setLikeCounts, appUserId, postId) {
    fetch(api + "/appUsers/" + appUserId + "/posts/" + postId + "/like", {method: "DELETE", headers: basic(auth)})
        .then(response => {
            if (!response.ok) throw new Error(response.statusText);
            setLikeStatus(prev => ({...prev, [postId]: false}));
            setLikeCounts(prev => ({...prev, [postId]: Math.max((prev[postId] || 0) - 1, 0)}));
        })
        .catch(error => console.error("Error in unliking  post: ", error));
}

export function hasUserLikedPost(api, auth, setLikeStatus, appUserId, postId) {
    fetch(api + "/appUsers/" + appUserId + "/posts/" + postId + "/like", {headers: basic(auth)})
        .then(response => {
            if (!response.ok) throw new Error(response.statusText);
            return response.json();
        }).then(hasUserLikedPost => {
        setLikeStatus(prev => ({...prev, [postId]: hasUserLikedPost}));
    })
        .catch(error => console.error("Error in checking like status", error));
}

export function numberOfLikePost(api, auth, setLikeCounts, postId) {
    fetch(api + "/appUsers/posts/" + postId + "/likeCount", {headers: basic(auth)})
        .then(response => {
            if (!response.ok) throw new Error(response.statusText);
            return response.json();
        }).then(likeCount => {
        setLikeCounts(prev => ({...prev, [postId]: likeCount}));
    })
        .catch(error => console.error("Error in getting no of likes of a post: ", error));
}
