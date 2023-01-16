import { mergeResolvers } from "@graphql-tools/merge";
import CostResolver from "./CostResolver.js";
import UserResolver from "./UserResolver.js";
const resolvers = mergeResolvers([CostResolver, UserResolver]);
export default resolvers;
