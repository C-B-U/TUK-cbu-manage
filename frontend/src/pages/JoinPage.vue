<template>
    <v-container class="join-page">
        <v-row align-items="center" justify="center" class="join-row">
            <v-col cols="12" sm="8" md="6" lg="6">
                <div class="join-wrapper">
                    <h2 class="join-title">회원가입</h2>
                    <v-card-text>
                        <v-form @submit.prevent="handleJoin">
                            <!-- 이름 입력 -->
                            <v-text-field v-model="name" label="이름" placeholder="이름을 입력하세요" required variant="outlined"
                                dense></v-text-field>

                            <!-- 학번 입력 -->
                            <v-text-field v-model="studentId" label="학번" placeholder="학번을 입력하세요" required
                                variant="outlined" dense></v-text-field>

                            <!-- 닉네임 입력 + 합격자 확인 버튼 -->
                            <v-row align-items="center" justify="space-between">
                                <v-col cols="9">
                                    <v-text-field v-model="nickname" label="지원 시 닉네임"
                                        placeholder="지원 시 구글폼에 입력한 본인의 닉네임을 적어주세요" variant="outlined" dense
                                        hide-details></v-text-field>
                                </v-col>
                                <v-col cols="3" class="email-btn-col">
                                    <v-btn class="custom-btn" block @click="handleUserVerification">
                                        합격자 확인
                                    </v-btn>
                                </v-col>
                            </v-row>

                            <!-- 이메일 입력 + 이메일 인증 버튼 -->
                            <v-row align-items="center" justify="space-between">
                                <v-col cols="9">
                                    <v-text-field v-model="studentEmail" label="이메일 (@tukorea.ac.kr)"
                                        placeholder="학교 이메일을 입력해주세요.(@tukorea.ac.kr)" required variant="outlined" dense
                                        :error="emailError" :error-messages="emailErrorMessage"></v-text-field>
                                </v-col>
                                <v-col cols="3" class="email-btn-col">
                                    <v-btn class="custom-btn" block @click="handleEmailVerification">
                                        이메일 인증
                                    </v-btn>
                                </v-col>
                            </v-row>

                            <!-- 인증번호 입력 필드 -->
                            <v-row align-items="center" justify="space-between">
                                <v-col cols="9">
                                    <v-text-field v-if="isVerificationSent" v-model="verificationCode" label="인증번호"
                                        placeholder="인증번호를 입력하세요" required variant="outlined" dense hide-details
                                        ></v-text-field>
                                </v-col>
                                <v-col cols="3" class="email-btn-col">
                                    <v-btn v-if="isVerificationSent" class="custom-btn" block @click="handleCodeVerification">
                                        인증하기
                                    </v-btn>
                                </v-col>
                                </v-row>

                                <!-- 회원가입 버튼 -->
                                <v-btn type="submit" block large class="mt-4 font-weight-bold custom-btn">
                                    회원가입
                                </v-btn>
                        </v-form>
                    </v-card-text>
                </div>
            </v-col>
        </v-row>
    </v-container>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import useVerifyEmail from "@/hooks/useVerifyEmail";
import useVerifyUser from "@/hooks/useVerifyUser";

// 기본 입력값
const name = ref("");
const studentId = ref("");
const nickname = ref("");
const studentEmail = ref("");
const verificationCode = ref("");
const { verifyUser } = useVerifyUser();

// 이메일 인증 로직
const {
    emailError,
    emailErrorMessage,
    isVerificationSent,
    validateEmail,
    sendEmailToServer,
    verifyCodeWithServer,
} = useVerifyEmail();

// 이메일 인증 처리
const handleEmailVerification = async () => {
    if (!validateEmail(studentEmail.value)) {
        return;
    }

    const success = await sendEmailToServer(studentEmail.value);
    if (success) {
        alert("메일이 전송되었습니다!\n인증번호를 입력해주세요!");
    }
};

const handleUserVerification = async () => {
    const success = await verifyUser(name.value, studentId.value, nickname.value);
    if (success) {
        console.log("닉네임 인증 성공!");
    } else {
        console.log("닉네임 인증 실패.");
    }
};

// 회원가입 처리
const handleJoin = () => {
    if (!isVerificationSent.value) {
        alert("이메일 인증을 완료해주세요!");
        return;
    }

    console.log("회원가입 시도:", {
        name: name.value,
        studentId: studentId.value,
        nickname: nickname.value,
        studentEmail: studentEmail.value,
        verificationCode: verificationCode.value,
    });
};

// 인증코드 인증
const handleCodeVerification = async () => {
  if (!verificationCode.value) {
    alert("인증번호를 입력해주세요.");
    return;
  }

  const success = await verifyCodeWithServer(studentEmail.value, verificationCode.value);
  if (success) {
    alert("인증에 성공했습니다!");
  } else {
    alert("인증번호가 올바르지 않습니다. 다시 시도해주세요.");
  }
};

</script>

<style scoped>
.join-page {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 16px;
    box-sizing: border-box;
}

.join-row {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}

.join-title {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 10px;
    padding: 16px;
    color: #333;
    text-align: start;
}

.custom-btn {
    background-color: #515151;
    height: 56px;
    color: #fff;
    border-radius: 5px;
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
    font-size: 1rem;
    text-transform: uppercase;
    transition: transform 0.2s ease;
}

.v-btn {
    letter-spacing: normal !important;
}

.email-btn-col {
    display: flex;
    align-items: flex-start;
}
</style>