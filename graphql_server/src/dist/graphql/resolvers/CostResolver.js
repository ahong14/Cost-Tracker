const resolvers = {
    Query: {
        getCosts: async (_, { userId }, { dataSources }) => {
            return await dataSources.costsApi.getCost(userId);
        }
    }
};
export default resolvers;
