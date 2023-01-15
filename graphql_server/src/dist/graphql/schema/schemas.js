const typeDefs = `#graphql
    type Query {
        getCosts(userId: ID!): [Costs]
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
`;
export default typeDefs;
