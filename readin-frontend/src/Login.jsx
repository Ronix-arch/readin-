import { useContext, useState } from "react";
import { Api } from "./Context.js";
import { anonJson, makeBasic } from "./Headers.js";

export default function Login({ setAuth }) {
    const api = useContext(Api);
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [isNewUser, setIsNewUser] = useState(false);

    function logIn() {
        const authData = { name, password };
        fetch(api + "/appUsers/login", {
            method: "POST",
            headers: { "Authorization": makeBasic(authData) }
        })
        .then(response => {
            if (response.ok) return response.json();
            throw new Error("Error in logging in");
        })
        .then(user => {
            setAuth({ ...user, password, loggedIn: true });
        })
        .catch(error => console.error("Error in logging in: ", error));
    }

    function createUser() {
        const newUserData = { name, password };
        fetch(api + "/appUsers", {
            method: "POST",
            headers: anonJson(), // This is the fix: Do not send any Authorization header
            body: JSON.stringify(newUserData)
        })
        .then(response => {
            if (response.ok) return response.json();
            throw new Error("Error in creating user");
        })
        .then(user => {
            setAuth({ ...user, password, loggedIn: true });
        })
        .catch(error => console.error("Error in creating user: ", error));
    }

    return (
        <div>
            <h2>{isNewUser ? "Create New User" : "Log In"}</h2>
            <input type="text" placeholder="Username" value={name} onChange={e => setName(e.target.value)} />
            <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
            <button onClick={isNewUser ? createUser : logIn}>
                {isNewUser ? "Create User" : "Log In"}
            </button>
            <button onClick={() => setIsNewUser(!isNewUser)}>
                {isNewUser ? "Already have an account? Log In" : "Don't have an account? Sign Up"}
            </button>
        </div>
    );
}
