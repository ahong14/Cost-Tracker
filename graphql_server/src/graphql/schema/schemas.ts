const typeDefs = `#graphql
    scalar Upload
    
    type Query {
        getCosts(userId: Int!, sortDir: String!): [Costs]
    }

    type Query {
        getCostsWithFilter(userId: Int!, title: String, fromDate: String, toDate: String, sortDir: String!): [Costs]
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

    type CostResponse {
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
        ): CostResponse
    }

    type Mutation {
        deleteCost(
            userId: Int!
            costId: Int!
        ): CostResponse
    }

    type Mutation {
        uploadBatchCosts(file: Upload): CostResponse
    }
`;

export default typeDefs;
