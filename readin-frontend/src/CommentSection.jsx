import { useContext, useEffect, useState, useCallback } from "react";
import { Api } from "./Context.js";
import { basic, basicJson } from "./Headers.js";

function Comment({ comment, auth, onCommentAdded }) {
    const api = useContext(Api);
    const [likeCount, setLikeCount] = useState(0);
    const [hasLiked, setHasLiked] = useState(false);
    const [showReplies, setShowReplies] = useState(false);
    const [replies, setReplies] = useState([]);
    const [repliesPage, setRepliesPage] = useState(0);
    const [hasMoreReplies, setHasMoreReplies] = useState(true);

    useEffect(() => {
        fetch(`${api}/comments/${comment.id}/likes/count`, { headers: basic(auth) })
            .then(res => res.json())
            .then(setLikeCount);
        fetch(`${api}/comments/${comment.id}/likes/status`, { headers: basic(auth) })
            .then(res => res.json())
            .then(setHasLiked);
    }, [api, auth, comment.id]);

    const fetchReplies = useCallback(() => {
        fetch(`${api}/posts/${comment.postId}/comments/${comment.id}/replies?page=${repliesPage}&size=5`, { headers: basic(auth) })
            .then(res => res.json())
            .then(data => {
                setReplies(prev => [...prev, ...data.content]);
                setHasMoreReplies(!data.last);
            });
    }, [api, auth, comment.id, comment.postId, repliesPage]);

    useEffect(() => {
        if (showReplies) {
            fetchReplies();
        }
    }, [showReplies, fetchReplies]);

    const handleLike = () => {
        const method = hasLiked ? "DELETE" : "POST";
        fetch(`${api}/comments/${comment.id}/likes`, { method, headers: basic(auth) })
            .then(() => {
                setHasLiked(!hasLiked);
                setLikeCount(prev => hasLiked ? prev - 1 : prev + 1);
            });
    };

    return (
        <div style={{ marginLeft: "20px", borderLeft: "1px solid #ccc", paddingLeft: "10px", marginTop: "10px" }}>
            <p><strong>@{comment.authorName}</strong>: {comment.content}</p>
            <small>{new Date(comment.createdAt).toLocaleString()}</small>
            <div>
                <button onClick={handleLike}>{hasLiked ? "❤️" : "🤍"} {likeCount}</button>
                <button onClick={() => setShowReplies(!showReplies)}>
                    {showReplies ? "Hide Replies" : `Replies (${comment.replyCount})`}
                </button>
            </div>
            {showReplies && (
                <>
                    <CommentForm postId={comment.postId} parentCommentId={comment.id} auth={auth} onCommentAdded={() => {
                        setReplies([]);
                        setRepliesPage(0);
                        fetchReplies();
                    }} />
                    {replies.map(reply => (
                        <Comment key={reply.id} comment={reply} auth={auth} onCommentAdded={onCommentAdded} />
                    ))}
                    {hasMoreReplies && <button onClick={() => setRepliesPage(prev => prev + 1)}>Load More Replies</button>}
                </>
            )}
        </div>
    );
}

function CommentForm({ postId, parentCommentId = null, auth, onCommentAdded }) {
    const api = useContext(Api);
    const [content, setContent] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!content.trim()) return;

        const body = { content, parentCommentId };
        fetch(`${api}/posts/${postId}/comments`, {
            method: "POST",
            headers: basicJson(auth),
            body: JSON.stringify(body),
        })
        .then(response => {
            if (!response.ok) throw new Error("Failed to post comment");
            return response.json();
        })
        .then(() => {
            setContent("");
            onCommentAdded();
        })
        .catch(error => console.error("Error creating comment:", error));
    };

    return (
        <form onSubmit={handleSubmit} style={{ marginTop: "10px" }}>
            <textarea value={content} onChange={(e) => setContent(e.target.value)} placeholder="Write a comment..." required />
            <button type="submit">Submit</button>
        </form>
    );
}

export default function CommentSection({ postId, auth }) {
    const api = useContext(Api);
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    const fetchComments = useCallback(() => {
        fetch(`${api}/posts/${postId}/comments?page=${page}&size=5`, { headers: basic(auth) })
            .then(res => res.json())
            .then(data => {
                setComments(prev => [...prev, ...data.content]);
                setHasMore(!data.last);
            });
    }, [api, auth, postId, page]);

    useEffect(() => {
        fetchComments();
    }, [fetchComments]);

    const handleCommentAdded = () => {
        setComments([]);
        setPage(0);
        fetchComments();
    };

    return (
        <div>
            <h4>Comments</h4>
            <CommentForm postId={postId} auth={auth} onCommentAdded={handleCommentAdded} />
            {comments.map(comment => (
                <Comment key={comment.id} comment={comment} auth={auth} onCommentAdded={handleCommentAdded} />
            ))}
            {hasMore && <button onClick={() => setPage(prev => prev + 1)}>Load More Comments</button>}
        </div>
    );
}
