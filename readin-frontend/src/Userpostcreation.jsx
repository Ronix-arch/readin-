import {useContext, useState} from "react";
import {Api} from "./Context.js";
import {basicJson} from "./Headers.js";

export default function CreatePostCreation({auth, updatePosts}) {
    const api = useContext(Api);
    const userId = auth.id;
    const [newPost, setNewPost] = useState("");


    function handleInputChange(event) {
        setNewPost(event.target.value);
    }


    function createPost(appUserId) {
        const newuserPost = {content: newPost};
        fetch(api + "/appUsers/" + appUserId + "/posts", {
            headers: basicJson(auth),
            method: "POST",
            body: JSON.stringify(newuserPost)
        })
            .then(response => {
                if (response.ok) return response.json(); else throw new Error(response.statusText);
            }).then(result => {
            updatePosts(prevPosts => [result, ...prevPosts]);
            setNewPost(""); //clear the input after creating a post
        }).catch(error => console.error("Error in creating  post: ", error));


    }


    return (<div>
            <p>Enter your post below ✍️</p>
            <input
                type="text"
                placeholder="What is on your Mind? Write about it"
                value={newPost}
                onChange={handleInputChange}
            />
            <button onClick={() => createPost(userId)}>Create Post</button>
        </div>);


}

