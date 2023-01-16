// reference https://www.apollographql.com/docs/apollo-server/api/express-middleware
import { ApolloServer } from "@apollo/server";
import { expressMiddleware } from "@apollo/server/express4";
import { ApolloServerPluginDrainHttpServer } from "@apollo/server/plugin/drainHttpServer";
import typeDefs from "./graphql/schema/schemas.js";
import resolvers from "./graphql/resolvers/resolvers.js";
import express from "express";
import http from "http";
import bodyParser from "body-parser";
import cors from "cors";
import morgan from "morgan";
// setup express server
const app = express();
const httpServer = http.createServer(app);
const PORT = process.env.port || 4000;
app.use(morgan("combined"));
// setup graphql server
// logging plugin
// reference: https://www.apollographql.com/docs/apollo-server/monitoring/metrics/#logging
const loggingPlugin = {
    // Fires whenever a GraphQL request is received from a client.
    async requestDidStart(requestContext) {
        console.log("Request started! Query:\n" + requestContext.request.query);
    }
};
const graphqlServer = new ApolloServer({
    typeDefs,
    resolvers,
    plugins: [ApolloServerPluginDrainHttpServer({ httpServer }), loggingPlugin]
});
await graphqlServer.start();
// map endpoint to /graphql server
app.use("/graphql", bodyParser.json(), cors(), expressMiddleware(graphqlServer));
await new Promise((resolve) => httpServer.listen({
    port: PORT
}, resolve));
console.log(`GraphQL server started at port ${PORT}`);
