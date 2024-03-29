import axios from "axios";
import GraphQLUpload from "graphql-upload/GraphQLUpload.mjs";
import fs from "fs";
import path from "path";

const resolvers = {
    Upload: GraphQLUpload,
    Query: {
        // reference: https://www.apollographql.com/docs/apollo-server/data/resolvers
        // params (parent, args, contextValue info)
        getCosts(_, { userId, sortDir }) {
            const getCostsFromAPI = async () => {
                let results = [];
                try {
                    const apiResults = await axios.get(
                        `http://localhost:8080/api/cost?userId=${userId}&sortDir=${sortDir}`
                    );
                    results = apiResults.data;
                    return results;
                } catch (err) {
                    console.error(err);
                    return results;
                }
            };
            return getCostsFromAPI();
        },
        getCostsWithFilter(_, { userId, title, fromDate, toDate, sortDir }) {
            const getCostsWithFilterFromAPI = async () => {
                let results = [];
                try {
                    let GET_COST_API_URL = `http://localhost:8080/api/cost?userId=${userId}&sortDir=${sortDir}`;
                    // if title filter selected
                    if (title) {
                        GET_COST_API_URL += `&title=${title}`;
                    }
                    // if from/to date filter selected
                    if (fromDate && toDate) {
                        GET_COST_API_URL += `&fromDate=${
                            new Date(fromDate).getTime() / 1000
                        }`;
                        GET_COST_API_URL += `&toDate=${
                            new Date(toDate).getTime() / 1000
                        }`;
                    }

                    const apiResults = await axios.get(GET_COST_API_URL);
                    results = apiResults.data;
                    return results;
                } catch (err) {
                    console.error(err);
                    return results;
                }
            };
            return getCostsWithFilterFromAPI();
        }
    },

    Mutation: {
        createCost(_, { amount, date, quantity, title, user_id }) {
            const createCostAPI = async () => {
                try {
                    const apiResults = await axios.post(
                        `http://localhost:8080/api/cost`,
                        {
                            amount: amount,
                            date: date,
                            quantity: quantity,
                            title: title,
                            user_id: user_id
                        }
                    );
                    const results = {
                        success: true,
                        data: apiResults.data
                    };
                    return results;
                } catch (err) {
                    console.error(err);
                    return "Failed to create cost";
                }
            };

            return createCostAPI();
        },
        deleteCost(_, { userId, costId }) {
            const deleteCostAPI = async () => {
                const apiResults = await axios.delete(
                    `http://localhost:8080/api/cost?userId=${userId}&costId=${costId}`
                );
                const results = {
                    success: true,
                    data: apiResults.data
                };
                return results;
            };
            return deleteCostAPI();
        },
        uploadBatchCosts(_, { file }) {
            const saveBatchCostsFile = async () => {
                const { createReadStream, filename, encoding, mimetype } =
                    await file;

                // TODO
                // saving to local filesystem, but file would be uploaded to external file storage
                // const stream = createReadStream();

                const results = {
                    success: true,
                    data: "Test"
                };
                return results;
            };

            return saveBatchCostsFile();
        }
    }
};

export default resolvers;
