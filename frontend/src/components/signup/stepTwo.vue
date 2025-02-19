<template>
  <div>
    <v-row>
      <v-col cols="6">
        <v-text-field
          class="rounded-input"
          v-model="userStore.name"
          label="이름"
          disabled
          variant="outlined"
          dense
        ></v-text-field>
      </v-col>
      <v-col cols="6">
        <v-text-field
          class="rounded-input"
          v-model="userStore.studentNumber"
          label="학번"
          disabled
          variant="outlined"
          dense
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6">
        <v-text-field
          class="rounded-input"
          v-model="userStore.major"
          label="학과"
          disabled
          variant="outlined"
          dense
        ></v-text-field>
      </v-col>
      <v-col cols="6">
        <v-text-field
          class="rounded-input"
          v-model="userStore.grade"
          label="학년"
          disabled
          variant="outlined"
          dense
        ></v-text-field>
      </v-col>
    </v-row>

        <!-- 이메일 입력 + 이메일 인증 버튼 -->
        <v-row align-items="center" justify="space-between">
            <v-col cols="9">
                <v-text-field class="rounded-input" v-model="studentEmail" label="이메일" suffix="@tukorea.ac.kr"
                    placeholder="학교 이메일을 입력해주세요." required variant="outlined" dense :error="emailError"
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
                    required variant="outlined" dense :class="{
                        'success-field': verificationStatus === 'success',
                        'error-field': verificationStatus === 'error',
                    }" :color="verificationStatus === 'success'
                ? 'green'
                : verificationStatus === 'error'
                    ? 'red'
                    : ''
            " :error="verificationStatus === 'error'" :success="verificationStatus === 'success'" :error-messages="verificationStatus === 'error' ? [verificationMessage] : []
            " :success-messages="verificationStatus === 'success' ? [verificationMessage] : []
            ">
                    <template v-slot:message>
                        <span v-if="verificationMessage" :class="verificationStatus === 'success' ? 'success-text' : 'error-text'
                            ">
                            {{ verificationMessage }}
                        </span>
                    </template>
                </v-text-field>
            </v-col>
            <v-col cols="3" class="email-btn-col">
                <v-btn class="custom-btn" block @click="handleCodeVerification">
                    인증하기
                </v-btn>
            </v-col>
        </v-row>

        <!-- 회원가입 버튼 -->
        <v-btn type="submit" block large class="mt-4 font-weight-bold custom-btn"  :disabled="!isJoinEnabled" @click="handleJoin">
            회원가입
        </v-btn>
    </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import useVerifyEmail from "@/hooks/useVerifyEmail";
import { useUserStore } from "@/stores/userStore";
import useSignUp from "@/hooks/useSignUp";

// 이메일 및 인증번호 입력값 관리
const studentEmail = ref("");
const verificationCode = ref("");

// 추가: 인증 상태를 관리 ('' 또는 'success' 또는 'error')
const verificationStatus = ref<"success" | "error" | "">("");
const verificationMessage = ref("");

const userStore = useUserStore();

// 이메일 인증 관련 hook
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
        alert("메일이 전송되었습니다!\n인증번호를 입력해주세요!");
    }
};

const isJoinEnabled = ref(false); // 회원가입 버튼 활성화 여부

const handleCodeVerification = async () => {
    if (!verificationCode.value) {
        verificationStatus.value = "error";
        verificationMessage.value = "인증번호를 입력해주세요.";
        return;
    }

    const result = await verifyCodeWithServer(
        studentEmail.value,
        verificationCode.value
    );
    if (result && typeof result === "object" && "success" in result) {
        verificationStatus.value = result.success ? "success" : "error";
        verificationMessage.value = result.responseMessage || "인증되었습니다.";

        if (result.success) {
            isJoinEnabled.value = true; // 회원가입 버튼 활성화
        } else {
            isJoinEnabled.value = false; // 인증 실패 시 회원가입 버튼 비활성화
        }
    } else {
        verificationStatus.value = "error";
        verificationMessage.value = "서버 응답 오류";
        isJoinEnabled.value = false;
    }
};

const { signUpErrorMessage, isSignUpSuccessful, registerUser } = useSignUp();
const handleJoin = async () => {
    if (!isVerificationSent.value) {
        alert("이메일 인증을 완료해주세요!");
        return;
    }

    const emailWithSuffix = studentEmail.value.includes("@") 
        ? studentEmail.value 
        : `${studentEmail.value}@tukorea.ac.kr`;

    console.log("📢 회원가입 버튼 클릭 - 요청 데이터:");
    console.log("Email:", emailWithSuffix);
    console.log("Verification Code:", verificationCode.value);
    console.log("Password:", "1234 (기본값)");

    await registerUser(
        emailWithSuffix,
        userStore.studentNumber,
        userStore.name,
        userStore.nickName
    );

    if (isSignUpSuccessful.value) {
        alert("🎉 회원가입이 완료되었습니다!");
    } else {
        alert(`🚨 회원가입 실패: ${signUpErrorMessage.value}`);
    }
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

/* 실패 시: 빨간색 테두리 및 안내 메시지 */
.error-field .v-field__outline {
  border: 2px solid red !important;
}

.error-text {
  color: red !important;
  font-weight: bold;
}
</style>
