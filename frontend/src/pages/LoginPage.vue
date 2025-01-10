<template>
    <div class="login-page">
        <h2>Login</h2>
        <form @submit.prevent="handleLogin">
            <div>
                <label for="username">Username:</label>
                <input type="text" v-model="username" id="username" required />
            </div>
            <div>
                <label for="password">Password:</label>
                <input type="password" v-model="password" id="password" required />
            </div>
            <button type="submit">Login</button>
        </form>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useLogin } from '../hooks/useLogin';

const username = ref('');
const password = ref('');
const { errorMessage, handleLogin: login } = useLogin();

const handleLogin = async () => {
    await login({ username: username.value, password: password.value });
};
</script>

<style scoped>
.login-page {
    max-width: 400px;
    margin: auto;
}

.error {
    color: red;
}
</style>
