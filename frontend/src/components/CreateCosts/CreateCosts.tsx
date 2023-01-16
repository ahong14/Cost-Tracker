import React, { useState } from "react";
// reference: https://mui.com/x/react-date-pickers/getting-started/#react-components
import dayjs, { Dayjs } from "dayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DesktopDatePicker } from "@mui/x-date-pickers/DesktopDatePicker";
import { useMutation, gql } from "@apollo/client";
import {
    Button,
    Container,
    FormControl,
    Input,
    InputLabel,
    InputAdornment,
    Stack,
    TextField,
    Typography
} from "@mui/material";

const CreateCosts = () => {
    const CREATE_COST = gql`
        mutation CreateCost(
            $amount: Float
            $date: String
            $quantity: Int
            $title: String
            $userId: Int
        ) {
            createCost(
                amount: $amount
                date: $date
                quantity: $quantity
                title: $title
                user_id: $userId
            )
        }
    `;
    const [title, setTitle] = useState<string>("");
    const [amount, setAmount] = useState<number>(0);
    const [quantity, setQuantity] = useState<number>(1);
    const [selectedDate, setSelectedDate] = useState<Dayjs | null>(
        dayjs(new Date())
    );

    const onTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setTitle(event.target.value);
    };

    const onAmountChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setAmount(parseFloat(event.target.value));
    };

    const onQuantityChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setQuantity(parseInt(event.target.value, 10));
    };

    const onDateChange = (newDateSelected: Dayjs | null) => {
        setSelectedDate(newDateSelected);
    };

    const onSubmitNewCost = () => {
        const newCost = {
            amount: amount,
            date: selectedDate,
            quantity: quantity,
            title: title,
            userId: 1
        };

        createCost({
            variables: {
                ...newCost
            }
        });
    };

    const [createCost, { loading, error, data }] = useMutation(CREATE_COST);
    return (
        <Container>
            <Typography variant="h5"> Create Cost </Typography>
            <Stack
                sx={{
                    border: "1px solid grey",
                    borderRadius: "10px",
                    marginTop: "20px"
                }}
            >
                <FormControl sx={{ m: 1, width: "25ch" }} variant="standard">
                    <InputLabel>Title</InputLabel>
                    <Input onChange={onTitleChange} />
                </FormControl>
                <FormControl sx={{ m: 1, width: "25ch" }} variant="standard">
                    <InputLabel>Amount</InputLabel>
                    <Input
                        startAdornment={
                            <InputAdornment position="start">$</InputAdornment>
                        }
                        onChange={onAmountChange}
                        type="number"
                    />
                </FormControl>
                <FormControl sx={{ m: 1, width: "25ch" }} variant="standard">
                    <InputLabel>Quantity</InputLabel>
                    <Input
                        onChange={onQuantityChange}
                        type="number"
                        defaultValue={1}
                        value={quantity}
                    />
                </FormControl>
                <FormControl sx={{ m: 1, width: "25ch" }} variant="standard">
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DesktopDatePicker
                            inputFormat="YYYY-MM-DD"
                            value={selectedDate}
                            onChange={onDateChange}
                            renderInput={(params) => <TextField {...params} />}
                        />
                    </LocalizationProvider>
                </FormControl>
                <FormControl sx={{ m: 1, width: "25ch" }} variant="standard">
                    <Button onClick={onSubmitNewCost}> Submit</Button>
                </FormControl>
            </Stack>
        </Container>
    );
};

export default CreateCosts;
