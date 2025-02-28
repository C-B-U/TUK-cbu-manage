<template>
    <v-container class="changea-mail-page">
        <v-card class="password-change-container">
            <v-card-title class="text-h5" style="margin-bottom: 10px; font-weight: 700;">비밀번호 변경 안내</v-card-title>
            <v-card-subtitle style="margin-bottom: 20px;">보안을 위해 비밀번호를 변경해주세요.</v-card-subtitle>

            <v-card-text>
                <v-form>
                    <v-text-field label="새 비밀번호" v-model="newPassword" :type="showPassword ? 'text' : 'password'"
                        placeholder="새 비밀번호 입력" outlined dense class="password-input">
                        <template v-slot:append>
                            <v-icon @click="showPassword = !showPassword">
                                {{ showPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                            </v-icon>
                        </template>
                    </v-text-field>
                    <small class="password-hint">🔹 8자 이상, 영어+숫자+특수문자 중 2개 이상 포함</small>

                    <v-text-field label="새 비밀번호 확인" v-model="confirmPassword"
                        :type="showConfirmPassword ? 'text' : 'password'" placeholder="새 비밀번호 확인" outlined dense>
                        <template v-slot:append>
                            <v-icon @click="showConfirmPassword = !showConfirmPassword">
                                {{ showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                            </v-icon>
                        </template>
                    </v-text-field>
                </v-form>
            </v-card-text>

            <v-card-actions>
                <v-btn color="primary" block :disabled="!isPasswordValid || newPassword !== confirmPassword"
                    @click="changePassword" class="large-button">
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
        console.log("📢 비밀번호 변경 요청 시작");
        console.log("👉 서버로 보낼 studentNumber:", studentNumber.value);
        console.log("👉 서버로 보낼 password:", newPassword.value);
        // ✅ "cbu" 접두사를 제거한 학번 추출
        
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
    width: 100%;
    height: 100%;
    padding: 0;
    box-sizing: border-box;
}

.password-change-container {
    padding: 40px;
    width: 90%;
    text-align: center;
    background-color: #fff;
    box-shadow: none;
}

.password-hint {
    display: block;
    text-align: left;
    color: #555;
    font-size: 14px;
    margin-top: -30px;
    margin-bottom: 40px;
}

.password-input {
    width: 100%;
    margin-bottom: 20px;
}
.large-button {
    font-size: 18px;
    padding: 14px;
    letter-spacing: normal;
}
</style>
