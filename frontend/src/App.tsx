import React from "react";
import { Route, Routes } from "react-router-dom";
import NavBar from "./components/NavBar/NavBar";
import ViewCosts from "./components/ViewCosts/ViewCosts";
import CreateCosts from "./components/CreateCosts/CreateCosts";
import "./App.css";

function App() {
    return (
        <>
            <NavBar />
            <Routes>
                <Route path="/viewCosts" element={<ViewCosts />} />
                <Route path="/createCosts" element={<CreateCosts />} />
            </Routes>
        </>
    );
}

export default App;
