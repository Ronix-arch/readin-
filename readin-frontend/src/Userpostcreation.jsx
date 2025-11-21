import {useContext, useState} from "react";
import {Api} from "./Context.js";
import {basic} from "./Headers.js";

export default function CreatePostCreation({auth, updatePosts}) {
    const api = useContext(Api);
    const userId = auth.id;
    const [newPost, setNewPost] = useState("");
    const [file, setFile] = useState(null);


    function handleInputChange(event) {
        setNewPost(event.target.value);
    }

    function handleFileChange(event) {
        setFile(event.target.files[0]);
    }


    function createPost(appUserId) {
        const formData = new FormData();
        formData.append("content", newPost);
        if (file) {
            formData.append("file", file);
        }

        fetch(api + "/appUsers/" + appUserId + "/posts", {
            headers: basic(auth),
            method: "POST",
            body: formData
        })
            .then(response => {
                if (response.ok) return response.json(); else throw new Error(response.statusText);
            }).then(result => {
            updatePosts(prevPosts => [result, ...prevPosts]);
            setNewPost(""); //clear the input after creating a post
            setFile(null); //clear the file input
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
            <input
                type="file"
                onChange={handleFileChange}
            />
            <button onClick={() => createPost(userId)}>Create Post</button>
        </div>);


}
