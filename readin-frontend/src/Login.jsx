import{useContext, useRef,useState} from 'react';
import {Api} from "./Context.js";
import {basic, anonJson} from "./Headers.js";

export default function Login(auth, setAuth) {
    const api = useContext(Api);
    const [createAccount, setCreateAccount] = useState(false);
    const name = useRef(undefined);
    const password = useRef(undefined);

    function logOut(){
        fetch(api + "/appUsers/logout",{method: "POST",headers:basic(auth)}).then(response => {
            if (!response.ok) throw new Error(response.statusText);
        }).then(()=>{
            setAuth({name: null, password: null, loggedIn: false});
            // is set use r possible?

        });
    }

    function logIn(){
        const newAuth = {name: name.current.value, password: password.current.value};
        fetch(api+"/appUsers/login",{method: "POST",headers:basic(newAuth)}).then(response => {
            if (response.ok) return response.json();
            else throw new Error(response.statusText);
        }).then(() => {

            newAuth.loggedIn = true;
            setAuth(newAuth);
        });
    }
    function register(){
        const newAuth = {name: name.current.value, password: password.current.value};
        fetch(api + "/appUsers",{method: "POST",headers: anonJson(), body: JSON.stringify(newAuth)}).then(response => {
            if (response.ok) return response.json();
            else throw new Error(response.statusText);
        }).then(() => {
            newAuth.loggedIn = true;
            setAuth(newAuth);
        });
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
            <input id = "new-account" type="checkbox" defaultValue={createAccount}
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
