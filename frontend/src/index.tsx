import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import { BrowserRouter as Router } from "react-router-dom";
import { ApolloClient, InMemoryCache, ApolloProvider } from "@apollo/client";
import { createUploadLink } from "apollo-upload-client";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

// graphql setup
const uploadLink = createUploadLink({
    uri: "http://localhost:4000/graphql",
    headers: { "Apollo-Require-Preflight": "true" }
});

const client = new ApolloClient({
    link: uploadLink,
    cache: new InMemoryCache()
});

root.render(
    <React.StrictMode>
        <ApolloProvider client={client}>
            <Router>
                <App />
            </Router>
        </ApolloProvider>
    </React.StrictMode>
);
