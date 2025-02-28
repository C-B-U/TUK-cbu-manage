<template>
    <v-container class="change-password-page">
        <v-card class="password-change-container">
            <v-card-title class="text-h5 title">비밀번호 변경 안내</v-card-title>
            <v-card-subtitle class="subtitle">보안을 위해 비밀번호를 변경해주세요.</v-card-subtitle>

            <v-card-text>
                <v-form>
                    <v-text-field 
                        label="새 비밀번호" 
                        v-model="newPassword" 
                        :type="showPassword ? 'text' : 'password'"
                        placeholder="새 비밀번호 입력" 
                        variant="outlined" dense class="password-input">
                        <template v-slot:append-inner>
                            <v-icon @click="showPassword = !showPassword" class="password-toggle-icon">
                                {{ showPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                            </v-icon>
                        </template>
                    </v-text-field>
                    <small class="password-hint">🔹 8자 이상, 영어+숫자+특수문자 중 2개 이상 포함</small>

                    <v-text-field 
                        label="새 비밀번호 확인" 
                        v-model="confirmPassword"
                        :type="showConfirmPassword ? 'text' : 'password'" 
                        placeholder="새 비밀번호 확인" 
                        variant="outlined" dense class="password-input">
                        <template v-slot:append-inner>
                            <v-icon @click="showConfirmPassword = !showConfirmPassword" class="password-toggle-icon">
                                {{ showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                            </v-icon>
                        </template>
                    </v-text-field>
                </v-form>
            </v-card-text>

            <v-card-actions>
                <v-btn block :disabled="!isPasswordValid || newPassword !== confirmPassword"
                    @click="changePassword" class="custom-btn">
                    비밀번호 변경
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useRoute } from 'vue-router';

const SERVER_URL = import.meta.env.VITE_SERVER_URL;
const route = useRoute();
const userStore = useUserStore();

const studentNumber = ref(route.params.studentNumber || userStore.studentNumber);
const newPassword = ref('');
const confirmPassword = ref('');
const showPassword = ref(false);
const showConfirmPassword = ref(false);

const isPasswordValid = computed(() => {
    const lengthValid = newPassword.value.length >= 8;
    const hasLetter = /[a-zA-Z]/.test(newPassword.value);
    const hasDigit = /\d/.test(newPassword.value);
    const hasSpecialChar = /[^a-zA-Z\d]/.test(newPassword.value);
    return lengthValid && ((hasLetter && hasDigit) || (hasLetter && hasSpecialChar) || (hasDigit && hasSpecialChar));
});

const changePassword = async () => {
    if (isPasswordValid.value && newPassword.value === confirmPassword.value) {
        try {
            const response = await fetch(`${SERVER_URL}/v1/login/password`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify({
                    userId: studentNumber.value,
                    password: newPassword.value
                })
            });

            const data = await response.json();

            if (response.ok) {
                alert("✅ 비밀번호 변경 완료!");
            } else {
                alert(`❌ 오류 발생: ${data.message || '비밀번호 변경 실패'}`);
            }
        } catch (error) {
            console.error("❌ 네트워크 오류:", error);
            alert("❌ 네트워크 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }
};
</script>

<style scoped>

.change-password-page {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 16px;
    box-sizing: border-box;
}

.password-change-container {
    padding: 40px;
    width: 80%;
    text-align: center;
    background-color: #fff;
    box-shadow: none;
}

.title {
    font-size: 1.7rem;
    font-weight: bold;
    margin-bottom: 10px;
    color: #333;
}

.subtitle {
    margin-bottom: 20px;
}

.password-input {
    width: 100%;
    margin-bottom: 15px;
}

.password-hint {
    display: block;
    text-align: left;
    color: #555;
    font-size: 14px;
    margin-top: -20px;
    margin-bottom: 30px;
}

.password-toggle-icon {
    cursor: pointer;
    font-size: 22px;
}

.custom-btn {
    background-color: var(--mainColor);
    height: 50px;
    color: #fff;
    border-radius: 12px;
    box-shadow: none;
    font-size: 1rem;
    text-transform: uppercase;
    transition: transform 0.2s ease;
    letter-spacing: 0;
}

.custom-btn:hover {
    transform: scale(1.02);
}
</style>
