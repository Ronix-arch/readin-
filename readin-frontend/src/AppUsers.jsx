import { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Api } from "./Context.js";
import { basic } from "./Headers.js";

export default function Users({ auth, onNavigateToProfile }) {
    const api = useContext(Api);
    const [users, setUsers] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [searchResults, setSearchResults] = useState([]);

    useEffect(() => {
        fetch(`${api}/appUsers`, { headers: basic(auth) })
            .then(res => res.json())
            .then(data => setUsers(data.filter(u => u.id !== auth.id)));
    }, [api, auth]);

    const handleSearch = (query) => {
        setSearchQuery(query);
        if (query) {
            fetch(`${api}/appUsers?query=${query}`, { headers: basic(auth) })
                .then(res => res.json())
                .then(data => setSearchResults(data.filter(u => u.id !== auth.id)));
        } else {
            setSearchResults([]);
        }
    };

    return (
        <>
            <h1>Search Users</h1>
            <div>
                <p>Search User by User Name 🔍 </p>
                <input
                    type="text"
                    placeholder="Search for users..."
                    value={searchQuery}
                    onChange={(e) => handleSearch(e.target.value)}
                />
            </div>

            {searchResults.length > 0 ? (
                <div>
                    <h5>Search Results:</h5>
                    <ul>
                        {searchResults.map(u => (
                            <li key={u.id}>
                                <Link to={`/profile/${u.id}`}>{u.name}</Link>
                            </li>
                        ))}
                    </ul>
                </div>
            ) : (
                <div>
                    <h5>All Users:</h5>
                    <ul>
                        {users.map(u => (
                            <li key={u.id}>
                                <Link to={`/profile/${u.id}`}>{u.name}</Link>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </>
    );
}
