import axios from "axios";

const UserResolvers = {
    Query: {
        loginUser(_, { email, password }) {
            const loginUserAPI = async (email: string, password: string) => {
                try {
                    const loginResult = await axios.post(
                        "http://localhost:8080/api/auth/login",
                        {
                            email: email,
                            password: password
                        }
                    );
                    return loginResult.data;
                } catch (err) {
                    console.error(err);
                    return "Failed to login";
                }
            };

            return loginUserAPI(email, password);
        }
    },
    Mutation: {
        createUser(_, { email, password, first_name, last_name }) {
            const signupUserAPI = async (
                email: string,
                password: string,
                first_name: string,
                last_name: string
            ) => {
                try {
                    const signupResult = await axios.post(
                        "http://localhost:8080/api/auth/signup",
                        {
                            email: email,
                            password: password,
                            first_name: first_name,
                            last_name: last_name
                        }
                    );
                    return signupResult.data;
                } catch (err) {
                    console.error(err);
                    return "Failed to signup";
                }
            };

            return signupUserAPI(email, password, first_name, last_name);
        }
    }
};

export default UserResolvers;
