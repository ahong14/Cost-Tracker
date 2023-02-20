import React, { useEffect, useState } from "react";
import {
    Alert,
    Button,
    Card,
    CardActions,
    CardContent,
    CircularProgress,
    Typography
} from "@mui/material";
import { Cost } from "../../types/types";
import { useMutation, gql } from "@apollo/client";

const CostItem = (props: Cost) => {
    const DELETE_COST_GQL = gql`
        mutation DeleteCost($userId: Int!, $costId: Int!) {
            deleteCost(userId: $userId, costId: $costId) {
                success
                data
            }
        }
    `;

    const [showDeletedSuccess, setShowDeletedSuccess] =
        useState<boolean>(false);
    const [deleteCost, { loading, error, data }] = useMutation(DELETE_COST_GQL);
    const onDeleteCost = () => {
        deleteCost({
            variables: {
                userId: props.user_id,
                costId: props.id
            }
        });

        // get costs after deletion
        if (props.refetchCosts && !props.isFiltered) {
            props.refetchCosts();
        } else if (props.isFiltered && props.refetchCostsFilter) {
            props.refetchCostsFilter();
        }
    };

    // TODO have parent component re-render, get updated costs
    useEffect(() => {
        if (data && data.deleteCost && data.deleteCost.success) {
            setShowDeletedSuccess(true);
            setTimeout(() => {
                setShowDeletedSuccess(false);
            }, 3000);
        }
    }, [data, error]);
    return (
        <Card sx={{ border: "solid 1px grey", borderRadius: "10px" }}>
            <CardContent>
                <Typography variant="h6">{props.title}</Typography>
                <Typography> ${props.amount.toFixed(2)}</Typography>
                <Typography> Date: {props.date}</Typography>
                <Typography> Quantity: {props.quantity}</Typography>
            </CardContent>

            <CardActions>
                <Button onClick={onDeleteCost} color="error">
                    Delete Cost
                </Button>
                {loading ? <CircularProgress /> : ""}
                {showDeletedSuccess ? (
                    <Alert
                        severity="success"
                        sx={{ width: "200px", marginTop: "20px" }}
                    >
                        Cost deleted successfully
                    </Alert>
                ) : (
                    ""
                )}
            </CardActions>
        </Card>
    );
};

export default CostItem;
