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
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { Dayjs } from "dayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DesktopDatePicker } from "@mui/x-date-pickers/DesktopDatePicker";
import CostItem from "../Cost/Cost";
import { Cost, CostFilterType } from "../../types/types";

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

    const GET_COSTS_WITH_FILTER = gql`
        query GetCostsWithFilter(
            $userId: Int!
            $title: String
            $fromDate: String
            $toDate: String
        ) {
            getCostsWithFilter(
                userId: $userId
                title: $title
                fromDate: $fromDate
                toDate: $toDate
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

    const userId = 1;
    const { loading, error, data } = useQuery(GET_COSTS, {
        variables: { userId },
        fetchPolicy: "no-cache"
    });

    const dataFilter = useQuery(GET_COSTS_WITH_FILTER, {
        variables: { userId, ...costFilter },
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

    const submitCostsFilter = () => {
        const newCostFilter: CostFilterType = {
            title: titleFilter,
            fromDate: fromDate,
            toDate: toDate
        };
        setCostFilter(newCostFilter);
    };

    const clearCostsFilter = () => {
        setTitleFilter(null);
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
        <Container maxWidth="lg">
            <Typography variant="h5"> Costs </Typography>
            <Accordion sx={{ width: "50%" }}>
                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
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
            {renderCosts && renderCosts.length ? renderCosts : "No costs found"}
        </Container>
    );
};

export default ViewCosts;
