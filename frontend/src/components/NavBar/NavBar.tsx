import React from "react";
import {
    AppBar,
    Box,
    Button,
    Container,
    Toolbar,
    Typography
} from "@mui/material";
import PaidIcon from "@mui/icons-material/Paid";
import { Link } from "react-router-dom";

// reference: https://mui.com/material-ui/react-app-bar/
const NavBar = () => {
    return (
        <AppBar position="fixed" sx={{ bgcolor: "green" }}>
            <Container maxWidth="xl">
                <Toolbar disableGutters>
                    <PaidIcon
                        sx={{ display: { xs: "none", md: "flex" }, mr: 1 }}
                    />

                    <Typography noWrap> Cost Tracker </Typography>
                    <Box
                        sx={{
                            flexGrow: 1,
                            display: { xs: "none", md: "flex" }
                        }}
                    >
                        <Link
                            to="/viewCosts"
                            style={{
                                textDecoration: "none",
                                color: "inherit"
                            }}
                        >
                            <Button
                                sx={{ my: 2, color: "white", display: "block" }}
                            >
                                View Costs
                            </Button>
                        </Link>

                        <Link
                            to="/createCosts"
                            style={{
                                textDecoration: "none",
                                color: "inherit"
                            }}
                        >
                            <Button
                                sx={{ my: 2, color: "white", display: "block" }}
                            >
                                Create Costs
                            </Button>
                        </Link>
                        <Link
                            to="/createBatchCosts"
                            style={{
                                textDecoration: "none",
                                color: "inherit"
                            }}
                        >
                            <Button
                                sx={{ my: 2, color: "white", display: "block" }}
                            >
                                Create Batch Costs
                            </Button>
                        </Link>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
};

export default NavBar;
