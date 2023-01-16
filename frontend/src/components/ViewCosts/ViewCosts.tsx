import React, { useState, useEffect } from "react";
import { useQuery, gql } from "@apollo/client";
import { Container, Typography } from "@mui/material";
import CostItem from "../Cost/Cost";
import { Cost } from "../../types/types";

const ViewCosts = () => {
    const GET_COSTS = gql`
        query GetCosts($userId: Int!) {
            getCosts(userId: $userId) {
                date
                amount
                date_unix
                id
                quantity
                title
                user_id
            }
        }
    `;
    const [costs, setCosts] = useState<Array<Cost>>([]);
    const [userId, setUserId] = useState<number | null>(null);
    const { loading, error, data } = useQuery(GET_COSTS, {
        variables: { userId }
    });

    useEffect(() => {
        setUserId(1);
    }, []);

    useEffect(() => {
        if (data && data.getCosts.length) {
            setCosts(data.getCosts);
        }
    }, [data]);

    const renderCosts = costs.map((renderCost: Cost) => {
        return <CostItem {...renderCost} />;
    });
    return (
        <Container maxWidth="lg">
            <Typography variant="h5"> Costs </Typography>
            {renderCosts}
        </Container>
    );
};

export default ViewCosts;
