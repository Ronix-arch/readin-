import{useContext, useRef,useState} from 'react';
import {Api} from "./Context.js";
import {basic, anonJson} from "./Headers.js";

export default function Login({auth, setAuth}) {
    const api = useContext(Api);
    const [createAccount, setCreateAccount] = useState(false);
    const name = useRef(undefined);
    const password = useRef(undefined);

    function logOut(){
        fetch(api + "/appUsers/logout",{method: "POST",headers:basic(auth)}).then(response => {
            if (!response.ok) throw new Error(response.statusText);
        }).then(()=>{
            setAuth({id: null,name: null, password: null, loggedIn: false});
            // is set use r possible?

        }).catch(error => console.error("Error logging out:", error));


    }

    function logIn(){
        const newAuth = {name: name.current.value, password: password.current.value};
        fetch(api+"/appUsers/login",{method: "POST",headers:basic(newAuth)})
            // .then(response => {
            // if (response.ok) return response.json();
            // else throw new Error(response.statusText);
            .then(response => response.text()) // Read raw response first
            .then(text => {
                console.log("Login response as text:", text);
                return JSON.parse(text); // Attempt parsing manually
            })


            .then(result  => {
            newAuth.id = result.id;   // that was the problem.
            newAuth.loggedIn = true;
            setAuth(newAuth);
        }).catch(error => console.error("Error logging in:", error));


    }
    function register(){
        const newAuth = {name: name.current.value, password: password.current.value};
        fetch(api + "/appUsers" ,{method: "POST",headers: anonJson(), body: JSON.stringify(newAuth)}).then(response => {
            if (response.ok) return response.json();
            else throw new Error(response.statusText);
        }).then(result => {
            newAuth.id = result.id; // also here
            newAuth.loggedIn = true;
            setAuth(newAuth);
        }).catch(error => console.error("Error in creating  an Account: ", error));
    }
    if (auth.loggedIn){
        return <>
            <p> Curently logged in as: {auth.name}</p>
            <button onClick={logOut}>Log Out</button>
        </>;
}else {
    return<>
        <p> Curently not logged in.</p>
        <div>
            <input id = "new-account" type="checkbox" checked={createAccount}
                   onChange={e =>setCreateAccount(e.target.checked)} />
            <label htmlFor="new-account">I want to create a new account </label>
        </div>
        <div className ="grid">
            <div>
                <label htmlFor= "name">User name:</label>
                <input id ="name" ref={name}/>
            </div>
        <div>
        <label htmlFor="password">Password:</label>
        <input type="password" id="password" ref={password}/>
        </div>
        </div>
        {createAccount ?
            <button onClick={register}>Register</button>
        : <button onClick={logIn}>Log in</button> }
    </>;
    }

}
