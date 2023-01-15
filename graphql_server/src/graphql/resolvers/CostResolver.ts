import axios from "axios";

const resolvers = {
    Query: {
        // reference: https://www.apollographql.com/docs/apollo-server/data/resolvers
        // params (parent, args, contextValue info)
        getCosts(_, { userId }) {
            const getCostsFromAPI = async () => {
                let results = [];
                try {
                    const apiResults = await axios.get(
                        `http://localhost:8080/api/cost?userId=${userId}`
                    );
                    results = apiResults.data;
                    return results;
                } catch (err) {
                    console.error(err);
                    return results;
                }
            };
            return getCostsFromAPI();
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
                    const results = apiResults.data;
                    return results;
                } catch (err) {
                    console.error(err);
                    return "Failed to create cost";
                }
            };

            return createCostAPI();
        }
    }
};

export default resolvers;
