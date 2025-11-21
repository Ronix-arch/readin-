import {createContext} from "react";

// All API calls will now be relative to the frontend's origin,
// and Nginx will proxy them to the backend.
export const Api = createContext("/api");
