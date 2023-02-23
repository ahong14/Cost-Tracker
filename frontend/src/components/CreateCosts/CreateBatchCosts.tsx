import React, { useState } from "react";
import {
    Button,
    Container,
    FormControl,
    Input,
    InputLabel,
    Typography
} from "@mui/material";
import { useMutation, gql } from "@apollo/client";

const CreateBatchCosts = () => {
    const UPLOAD_FILE = gql`
        mutation uploadBatchCosts($file: Upload) {
            uploadBatchCosts(file: $file) {
                success
                data
            }
        }
    `;

    const [fileUpload] = useMutation(UPLOAD_FILE, {
        onCompleted: (data) => console.log(data)
    });

    const [uploadedFile, setUploadedFile] = useState<File | undefined | null>(
        null
    );

    const onFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUploadedFile((event.target as HTMLInputElement).files?.[0]);
    };

    const onUploadFile = () => {
        if (uploadedFile) {
            // send graphql mutation query with uploaded file as param
            console.log(uploadedFile);
            fileUpload({
                variables: {
                    file: uploadedFile
                }
            });
        }
    };

    // useEffect(() => {
    //     if (uploadedFile) {
    //         const fileReader = new FileReader();
    //         fileReader.readAsDataURL(uploadedFile as Blob);
    //         fileReader.onload = () => {
    //             setBase64File(fileReader.result);
    //         };

    //         fileReader.onerror = () => {
    //             console.error("Error reading file");
    //         };
    //     }
    // }, [uploadedFile]);

    return (
        <Container className="costContainer">
            <Typography variant="h5"> Create Batch Costs </Typography>
            <FormControl>
                <Input type="file" onChange={onFileChange}></Input>
                <InputLabel htmlFor="contained-button-file"></InputLabel>
                <Button
                    onClick={onUploadFile}
                    variant="contained"
                    color="primary"
                >
                    Upload
                </Button>
            </FormControl>
        </Container>
    );
};

export default CreateBatchCosts;
