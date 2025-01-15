import axios from 'axios';

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

/**
 * Fetch data from the server.
 * @returns {Promise<any>} The data from the server.
 */
export const fetchData = async () => {
    try {
        const response = await axios.get(`${SERVER_URL}/data`);
        return response.data;
    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
};

/**
 * Login user with credentials.
 * @param {string} username - The username of the user.
 * @param {string} password - The password of the user.
 * @returns {Promise<{ token: string; user: object }>} The response data.
 */
export const loginUser = async (username: string, password: string) => {
    try {
        const response = await axios.post(`${SERVER_URL}/login`, {
            username,
            password
        });
        return response.data;
    } catch (error) {
        console.error('Login failed:', error);
        throw error;
    }
};
