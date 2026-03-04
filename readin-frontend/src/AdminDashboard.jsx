import { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Api } from "./Context.js";
import { basic } from "./Headers.js";

export default function AdminDashboard({ auth }) {
    const api = useContext(Api);
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetchUsers();
    }, [api, auth]);

    const fetchUsers = () => {
        fetch(`${api}/appUsers`, { headers: basic(auth) })
            .then(res => res.json())
            .then(data => setUsers(data));
    };

    const deleteUser = (userName) => {
        if (!window.confirm("Are you sure you want to delete this user completely? This cannot be undone.")) return;
        fetch(`${api}/appUsers/${userName}`, {
            method: "DELETE",
            headers: basic(auth)
        }).then(res => {
            if (res.ok) fetchUsers();
            else alert("Failed to delete user.");
        });
    };

    return (
        <div>
            <h2>Admin Dashboard</h2>
            <p>Welcome, Admin! Here you can manage the application users.</p>
            <h3>All Users</h3>
            <div style={{ overflowX: 'auto' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', minWidth: '600px' }}>
                    <thead>
                        <tr>
                            <th style={{ borderBottom: '1px solid #ccc', padding: '12px' }}>ID</th>
                            <th style={{ borderBottom: '1px solid #ccc', padding: '12px' }}>Username</th>
                            <th style={{ borderBottom: '1px solid #ccc', padding: '12px' }}>Role</th>
                            <th style={{ borderBottom: '1px solid #ccc', padding: '12px' }}>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(u => (
                            <tr key={u.id}>
                                <td style={{ borderBottom: '1px solid #eee', padding: '12px' }}>{u.id}</td>
                                <td style={{ borderBottom: '1px solid #eee', padding: '12px' }}>{u.name}</td>
                                <td style={{ borderBottom: '1px solid #eee', padding: '12px' }}>{u.role}</td>
                                <td style={{ borderBottom: '1px solid #eee', padding: '12px', display: 'flex', gap: '8px' }}>
                                    <button onClick={() => navigate(`/profile/${u.id}`)} className="outline">View</button>
                                    <button onClick={() => navigate(`/messages/${u.id}`)} className="outline">Message</button>
                                    {u.id !== auth.id && <button onClick={() => deleteUser(u.name)} style={{ backgroundColor: '#e53935', borderColor: '#e53935', color: 'white' }}>Delete</button>}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
