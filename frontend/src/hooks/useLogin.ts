import { ref } from 'vue';

interface LoginParams {
    email: string;
    password: string;
}

export function useLogin() {
    const errorMessage = ref<string | null>(null);

    const handleLogin = async ({ email, password }: LoginParams): Promise<void> => {
        const SERVER_URL = process.env.VUE_APP_SERVER_URL;

        try {
            const response = await fetch(`${SERVER_URL}/users/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                errorMessage.value = errorData.message || 'Login failed';
                return;
            }

            errorMessage.value = null;
        } catch (error: any) {
            errorMessage.value = 'An error occurred during login.';
            console.error('Login error:', error);
        }
    };

    return {
        errorMessage,
        handleLogin,
    };
}
