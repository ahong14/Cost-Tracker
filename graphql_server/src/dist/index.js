// reference https://www.apollographql.com/docs/apollo-server/api/express-middleware
import { ApolloServer } from "@apollo/server";
import { expressMiddleware } from "@apollo/server/express4";
import { ApolloServerPluginDrainHttpServer } from "@apollo/server/plugin/drainHttpServer";
import typeDefs from "./graphql/schema/schemas.js";
import resolvers from "./graphql/resolvers/CostResolver.js";
import express from "express";
import http from "http";
import bodyParser from "body-parser";
const app = express();
const httpServer = http.createServer(app);
const PORT = process.env.port || 4000;
const graphqlServer = new ApolloServer({
    typeDefs,
    resolvers,
    plugins: [ApolloServerPluginDrainHttpServer({ httpServer })]
});
await graphqlServer.start();
// map endpoint to /graphql server
app.use("/graphql", bodyParser.json(), expressMiddleware(graphqlServer));
await new Promise((resolve) => httpServer.listen({
    port: PORT
}, resolve));
console.log(`GraphQL server started at port ${PORT}`);
