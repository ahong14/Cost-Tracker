// reference https://www.apollographql.com/docs/apollo-server/api/express-middleware

import { ApolloServer } from "@apollo/server";
import { expressMiddleware } from "@apollo/server/express4";
import { ApolloServerPluginDrainHttpServer } from "@apollo/server/plugin/drainHttpServer";

import express from "express";
import http from "http";
import bodyParser from "body-parser";
import axios from "axios";

const app = express();
const httpServer = http.createServer(app);

const PORT = process.env.port || 4000;

const typeDefs = `#graphql
    type Query {
        getCosts(userId: Int!): [Costs]
    }

    type Costs {
        id: Int,
        amount: Float,
        date: String,
        date_unix: Float,
        quantity: Int,
        title: String,
        user_id: Int
    }

    type Mutation {
        createCost(amount: Float, date: String, quantity: Int, title: String, user_id: Int): String
    }
`;

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

const graphqlServer = new ApolloServer({
    typeDefs,
    resolvers,
    plugins: [ApolloServerPluginDrainHttpServer({ httpServer })]
});

await graphqlServer.start();

app.use("/graphql", bodyParser.json(), expressMiddleware(graphqlServer));

await new Promise<void>((resolve) =>
    httpServer.listen(
        {
            port: PORT as number
        },
        resolve
    )
);
console.log(`GraphQL server started at port ${PORT}`);
