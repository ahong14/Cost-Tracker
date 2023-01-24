const typeDefs = `#graphql
    type Query {
        getCosts(userId: Int!): [Costs]
    }

    type Query {
        getCostsWithFilter(userId: Int!, title: String,fromDate: String, toDate: String): [Costs]
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

    type Query {
        loginUser(email: String, password: String) : LoginResponse
    }

    type User {
        first_name: String,
        last_name: String,
        email: String,
        password: String
    }

    type LoginResponse {
        loginToken: String,
        message: String
    }

    type CreateCostResponse {
        success: Boolean,
        data: String
    }

    type Mutation {
        createUser(
            first_name: String,
            last_name: String,
            email: String,
            password: String
        ) : String
    }

    type Mutation {
        createCost(
            amount: Float
            date: String
            date_unix: Float
            quantity: Int
            title: String
            user_id: Int
        ): CreateCostResponse
    }
`;

export default typeDefs;
