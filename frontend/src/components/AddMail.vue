<template>
    <v-container class="email-verification-page">
        <v-card class="email-verification-container">
            <v-card-title class="text-h5 title">학교 메일 인증</v-card-title>
            <v-card-subtitle class="subtitle">학교 이메일을 입력하고 인증을 완료하세요.</v-card-subtitle>

            <v-card-text>
                <template v-if="!userStore.emailUpdated">
                    <v-form>
                        <!-- 이메일 입력 + 인증번호 보내기 버튼 -->
                        <v-row>
                            <v-col cols="9" align="center">
                                <v-text-field class="rounded-input" v-model="studentEmail" label="학교 이메일"
                                    suffix="@tukorea.ac.kr" placeholder="학교 이메일을 입력하세요." required variant="outlined" dense
                                    :error="emailError" :error-messages="emailErrorMessage" hide-details="auto">
                                </v-text-field>
                            </v-col>
                            <v-col cols="3" class="email-btn-col">
                                <v-btn class="custom-btn" block @click="handleEmailVerification">
                                    인증번호 전송
                                </v-btn>
                            </v-col>
                        </v-row>

                        <!-- 인증번호 입력 필드 (이메일 전송 후 표시) -->
                        <v-row v-if="isVerificationSent" justify="space-between">
                            <v-col cols="9" align="center">
                                <v-text-field class="rounded-input" v-model="verificationCode" label="인증번호"
                                    placeholder="인증번호 입력" required variant="outlined" dense
                                    :error="verificationStatus === 'error'"
                                    :error-messages="verificationStatus === 'error' ? [verificationMessage] : []"
                                    hide-details="auto">
                                </v-text-field>
                            </v-col>
                            <v-col cols="3" class="email-btn-col">
                                <v-btn class="custom-btn" block @click="handleCodeVerification">
                                    인증하기
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>
                </template>

                <!-- ✅ 이메일 등록 완료 메시지 -->
                <template v-else>
                    <div class="success-message">
                        📩 이메일이 등록되었습니다: <strong>{{ userStore.email }}</strong>
                    </div>
                </template>
            </v-card-text>

            <!-- ✅ 이메일이 업데이트되지 않았을 때만 완료 버튼 표시 -->
            <v-card-actions v-if="!userStore.emailUpdated">
                <v-btn block :disabled="!isJoinEnabled" @click="handleComplete" class="custom-btn">
                    완료
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import useVerifyEmail from "../hooks/useVerifyEmail";
import { useUserStore } from '../stores/userStore';

const userStore = useUserStore();
const router = useRouter();
const SERVER_URL = import.meta.env.VITE_SERVER_URL;

// 이메일 입력값 및 상태 관리
const studentEmail = ref("");
const verificationCode = ref("");
const verificationStatus = ref<"success" | "error" | "">("");
const verificationMessage = ref("");
const isJoinEnabled = ref(false); // 완료 버튼 활성화 여부

// 이메일 인증 관련 hooks
const {
    emailError,
    emailErrorMessage,
    isVerificationSent,
    sendEmailToServer,
    verifyCodeWithServer,
} = useVerifyEmail();

const handleEmailVerification = async () => {
    const success = await sendEmailToServer(studentEmail.value);
    if (success) {
        alert("인증번호가 전송되었습니다!\n이메일을 확인해주세요.");
    }
};

const handleCodeVerification = async () => {
    if (!verificationCode.value) {
        verificationStatus.value = "error";
        verificationMessage.value = "인증번호를 입력해주세요.";
        return;
    }

    const result = await verifyCodeWithServer(studentEmail.value, verificationCode.value);

    if (!result || typeof result !== "object" || typeof result.success === "undefined") {
        verificationStatus.value = "error";
        verificationMessage.value = "서버 응답 오류";
        return;
    }

    if (result.success) {
        isJoinEnabled.value = true;
    } else {
        isJoinEnabled.value = false;
        alert(`인증 실패: ${result.responseMessage}`);
    }
};

// 완료 버튼 클릭 시 이메일 확인 및 이벤트 발생
const handleComplete = async () => {
    let emailWithSuffix = studentEmail.value.trim();

    // 사용자가 이메일을 입력했지만 '@tukorea.ac.kr'이 없으면 추가
    if (!emailWithSuffix.includes("@")) {
        emailWithSuffix += "@tukorea.ac.kr";
    }

    try {
        const response = await fetch(`${SERVER_URL}/v1/mail/update`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                studentNumber: userStore.studentNumber,
                email: emailWithSuffix,
            }),
        });

        if (response.ok) {
            userStore.updateEmail(emailWithSuffix); // 이메일을 store에 업데이트
            userStore.emailUpdated = true; // 이메일 업데이트 완료 상태 저장
            alert("📩 이메일이 성공적으로 등록되었습니다!");
        } else {
            alert('이메일 업데이트에 실패했습니다. 다시 시도해주세요.');
        }
    } catch (error) {
        alert('이메일 업데이트 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
};
</script>

<style scoped>
.email-verification-page {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 16px;
    box-sizing: border-box;
}

.email-verification-container {
    padding: 40px;
    width: 100%;
    text-align: center;
    background-color: #fff;
    border-radius: 12px;
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

.rounded-input {
    width: 100%;
    margin-bottom: 0;
}

.email-btn-col {
    display: flex;
    align-items: flex-start;
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

.success-message {
    font-size: 1.2rem;
    font-weight: bold;
    color: var(--mainColor);
    margin-top: 20px;
}
</style>
