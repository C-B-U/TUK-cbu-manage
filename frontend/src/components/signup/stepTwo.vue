<template>
    <div>
        <v-row>
            <v-col cols="6">
                <v-text-field class="rounded-input" v-model="userStore.name" label="이름" disabled variant="outlined"
                    dense></v-text-field>
            </v-col>
            <v-col cols="6">
                <v-text-field class="rounded-input" v-model="userStore.studentNumber" label="학번" disabled
                    variant="outlined" dense></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col cols="6">
                <v-text-field class="rounded-input" v-model="userStore.major" label="학과" disabled variant="outlined"
                    dense></v-text-field>
            </v-col>
            <v-col cols="6">
                <v-text-field class="rounded-input" v-model="userStore.grade" label="학년" disabled variant="outlined"
                    dense></v-text-field>
            </v-col>
        </v-row>


        <!-- 이메일 입력 + 이메일 인증 버튼 -->
        <v-row align-items="center" justify="space-between">
            <v-col cols="9">
                <v-text-field class="rounded-input" v-model="studentEmail" label="이메일 (@tukorea.ac.kr)"
                    placeholder="학교 이메일을 입력해주세요.(@tukorea.ac.kr)" required variant="outlined" dense :error="emailError"
                    :error-messages="emailErrorMessage"></v-text-field>
            </v-col>
            <v-col cols="3" class="email-btn-col">
                <v-btn class="custom-btn" block @click="handleEmailVerification">
                    이메일 인증
                </v-btn>
            </v-col>
        </v-row>

        <!-- 인증번호 입력 필드 (이메일 전송 후 표시) -->
        <v-row v-if="isVerificationSent" align-items="center" justify="space-between">
            <v-col cols="9">
                <v-text-field class="rounded-input" v-model="verificationCode" label="인증번호" placeholder="인증번호를 입력하세요"
                    required variant="outlined" dense hide-details></v-text-field>
            </v-col>
            <v-col cols="3" class="email-btn-col">
                <v-btn class="custom-btn" block @click="handleCodeVerification">
                    인증하기
                </v-btn>
            </v-col>
        </v-row>

        <!-- 회원가입 버튼 -->
        <v-btn type="submit" block large class="mt-4 font-weight-bold custom-btn" @click="handleJoin">
            회원가입
        </v-btn>
    </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import useVerifyEmail from '@/hooks/useVerifyEmail';
import { useUserStore } from '@/stores/userStore';

// 이메일 및 인증번호 입력값 관리
const studentEmail = ref('');
const verificationCode = ref('');
const userStore = useUserStore();

// 이메일 인증 관련 hook
const {
    emailError,
    emailErrorMessage,
    isVerificationSent,
    validateEmail,
    sendEmailToServer,
    verifyCodeWithServer,
} = useVerifyEmail();

const handleEmailVerification = async () => {
    if (!validateEmail(studentEmail.value)) {
        return;
    }
    const success = await sendEmailToServer(studentEmail.value);
    if (success) {
        alert('메일이 전송되었습니다!\n인증번호를 입력해주세요!');
    }
};

const handleCodeVerification = async () => {
    if (!verificationCode.value) {
        alert('인증번호를 입력해주세요.');
        return;
    }
    const success = await verifyCodeWithServer(studentEmail.value, verificationCode.value);
    if (success) {
        alert('인증에 성공했습니다!');
    } else {
        alert('인증번호가 올바르지 않습니다. 다시 시도해주세요.');
    }
};

const handleJoin = () => {
    if (!isVerificationSent.value) {
        alert('이메일 인증을 완료해주세요!');
        return;
    }
    console.log('회원가입 시도:', {
        studentEmail: studentEmail.value,
        verificationCode: verificationCode.value,
    });
};
</script>

<style scoped>
.email-btn-col {
    display: flex;
    align-items: flex-start;
}

.custom-btn {
    background-color: var(--mainColor);
    height: 56px;
    color: #fff;
    border-radius: 10px;
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
    font-size: 1rem;
    text-transform: uppercase;
    transition: transform 0.2s ease;
    letter-spacing: 0;
}

::v-deep .rounded-input .v-field__outline {
    border-radius: 10px;
}
</style>