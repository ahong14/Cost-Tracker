import React, { useState, useEffect } from "react";
import { useQuery, gql } from "@apollo/client";
import {
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Box,
    Button,
    CircularProgress,
    Container,
    FormControl,
    Input,
    InputLabel,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import ArrowUpwardIcon from "@mui/icons-material/ArrowUpward";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { Dayjs } from "dayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DesktopDatePicker } from "@mui/x-date-pickers/DesktopDatePicker";
import CostItem from "../Cost/Cost";
import { Cost, CostFilterType } from "../../types/types";

const ViewCosts = () => {
    const GET_COSTS = gql`
        query GetCosts($userId: Int!, $sortDir: String!) {
            getCosts(userId: $userId, sortDir: $sortDir) {
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

    const GET_COSTS_WITH_FILTER = gql`
        query GetCostsWithFilter(
            $userId: Int!
            $title: String
            $fromDate: String
            $toDate: String
            $sortDir: String!
        ) {
            getCostsWithFilter(
                userId: $userId
                title: $title
                fromDate: $fromDate
                toDate: $toDate
                sortDir: $sortDir
            ) {
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
    const [costFilter, setCostFilter] = useState<CostFilterType | null>(null);
    const [titleFilter, setTitleFilter] = useState<string | null>(null);
    const [fromDate, setFromDate] = useState<Dayjs | null>(null);
    const [toDate, setToDate] = useState<Dayjs | null>(null);
    const [sortDirection, setSortDirection] = useState<string>("asc");

    const userId = 1;
    const { loading, error, data } = useQuery(GET_COSTS, {
        variables: { userId: userId, sortDir: sortDirection },
        fetchPolicy: "no-cache"
    });

    const dataFilter = useQuery(GET_COSTS_WITH_FILTER, {
        variables: { userId, ...costFilter, sortDir: sortDirection },
        fetchPolicy: "no-cache"
    });

    const onTitleFilterChange = (
        event: React.ChangeEvent<HTMLInputElement>
    ) => {
        setTitleFilter(event.target.value);
    };

    const onFromDateChange = (newFromDate: Dayjs | null) => {
        setFromDate(newFromDate);
    };

    const onToDateChange = (newToDate: Dayjs | null) => {
        setToDate(newToDate);
    };

    const onSortDirectionChange = () => {
        sortDirection === "asc"
            ? setSortDirection("desc")
            : setSortDirection("asc");
    };

    const submitCostsFilter = () => {
        const newCostFilter: CostFilterType = {
            title: titleFilter,
            fromDate: fromDate,
            toDate: toDate
        };
        setCostFilter(newCostFilter);
    };

    const clearCostsFilter = () => {
        setTitleFilter("");
        setFromDate(null);
        setToDate(null);
        setCostFilter({});
    };

    // when initial data changes
    useEffect(() => {
        if (data && data.getCosts.length) {
            setCosts(data.getCosts);
        } else {
            setCosts([]);
        }
    }, [data]);

    // when filter changes
    useEffect(() => {
        if (dataFilter && dataFilter.data) {
            setCosts(dataFilter.data.getCostsWithFilter);
        } else {
            setCosts([]);
        }
    }, [dataFilter]);

    const renderCosts =
        !costs || !costs.length
            ? []
            : (costs || []).map((renderCost: Cost) => {
                  return <CostItem {...renderCost} />;
              });
    return (
        <Container maxWidth="lg" className="costContainer">
            <Typography variant="h5"> Costs </Typography>
            <Accordion sx={{ width: "50%" }}>
                <AccordionSummary
                    sx={{ marginTop: "10px", marginBottom: "10px" }}
                    expandIcon={<ExpandMoreIcon />}
                >
                    <Typography variant="h6"> Filters </Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Stack sx={{ marginTop: "-25px" }}>
                        <FormControl
                            sx={{ m: 1, width: "25ch" }}
                            variant="standard"
                        >
                            <InputLabel> Filter Title</InputLabel>
                            <Input
                                onChange={onTitleFilterChange}
                                value={titleFilter}
                            />
                        </FormControl>
                        <Box>
                            <FormControl
                                sx={{ m: 1, width: "25ch" }}
                                variant="standard"
                            >
                                <Typography> From Date: </Typography>
                                <LocalizationProvider
                                    dateAdapter={AdapterDayjs}
                                >
                                    <DesktopDatePicker
                                        inputFormat="YYYY-MM-DD"
                                        value={fromDate}
                                        onChange={onFromDateChange}
                                        renderInput={(params) => (
                                            <TextField {...params} />
                                        )}
                                    />
                                </LocalizationProvider>
                            </FormControl>
                            <FormControl
                                sx={{ m: 1, width: "25ch" }}
                                variant="standard"
                            >
                                <Typography> To Date: </Typography>
                                <LocalizationProvider
                                    dateAdapter={AdapterDayjs}
                                >
                                    <DesktopDatePicker
                                        inputFormat="YYYY-MM-DD"
                                        value={toDate}
                                        onChange={onToDateChange}
                                        renderInput={(params) => (
                                            <TextField {...params} />
                                        )}
                                    />
                                </LocalizationProvider>
                            </FormControl>
                        </Box>
                    </Stack>
                    <Button onClick={submitCostsFilter}> Apply Filters </Button>
                    <Button onClick={clearCostsFilter}> Clear Filters </Button>
                </AccordionDetails>
            </Accordion>
            {loading || (dataFilter && dataFilter.loading) ? (
                <CircularProgress />
            ) : (
                ""
            )}
            <Stack marginTop={2}>
                <Stack sx={{ width: "300px" }}>
                    <Typography> Sort Direction: </Typography>
                    <Button
                        onClick={onSortDirectionChange}
                        startIcon={
                            sortDirection === "asc" ? (
                                <ArrowUpwardIcon />
                            ) : (
                                <ArrowDownwardIcon />
                            )
                        }
                        size="medium"
                        sx={{ marginTop: "20px", marginBottom: "20px" }}
                        color="primary"
                    >
                        {sortDirection === "asc" ? "Ascending" : "Descending"}
                    </Button>
                </Stack>

                {renderCosts && renderCosts.length
                    ? renderCosts
                    : "No costs found"}
            </Stack>
        </Container>
    );
};

export default ViewCosts;
