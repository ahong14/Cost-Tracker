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
        createCost(
            amount: Float
            date: String
            date_unix: Float
            quantity: Int
            title: String
            user_id: Int
        ): String
    }
`;

export default typeDefs;
