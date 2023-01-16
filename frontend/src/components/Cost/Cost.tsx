import React from "react";
import { Card, CardContent, Typography } from "@mui/material";
import { Cost } from "../../types/types";

const CostItem = (props: Cost) => {
    return (
        <Card sx={{ border: "solid 1px grey", borderRadius: "10px" }}>
            <CardContent>
                <Typography variant="h6">{props.title}</Typography>
                <Typography> ${props.amount.toFixed(2)}</Typography>
                <Typography> Date: {props.date}</Typography>
                <Typography> Quantity: {props.quantity}</Typography>
            </CardContent>
        </Card>
    );
};

export default CostItem;
